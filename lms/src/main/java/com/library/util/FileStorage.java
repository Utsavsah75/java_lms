package com.library.util;

import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * Stores uploaded book covers / student photos (PHP: move_uploaded_file into uploads/).
 * Files go to upload.dir (outside the WAR) and are served by UploadServlet at /uploads/*.
 */
public final class FileStorage {

    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif");
    public static final long MAX_BYTES = 2L * 1024 * 1024;

    private FileStorage() {
    }

    public static Path uploadDir() {
        String configured = AppConfig.get("upload.dir", null);
        Path p = configured != null
                ? Paths.get(configured)
                : Paths.get(System.getProperty("user.home"), "library_uploads");
        return p.toAbsolutePath().normalize();
    }

    /** Saves an image part and returns the stored file name, or null when nothing was uploaded. */
    public static String saveImage(Part part) throws BusinessException, IOException {
        if (part == null || part.getSize() == 0) {
            return null;
        }
        if (part.getSize() > MAX_BYTES) {
            throw new BusinessException("The image is too big. Maximum size is 2 MB.");
        }
        String submitted = part.getSubmittedFileName() == null ? "" : part.getSubmittedFileName();
        int dot = submitted.lastIndexOf('.');
        String ext = dot < 0 ? "" : submitted.substring(dot + 1).toLowerCase(Locale.ROOT);
        if (!ALLOWED.contains(ext)) {
            throw new BusinessException("Invalid image format. Only jpg, jpeg, png and gif are allowed.");
        }
        byte[] head = new byte[12];
        int n;
        try (InputStream in = part.getInputStream()) {
            n = in.readNBytes(head, 0, head.length);
        }
        if (!looksLikeImage(head, n)) {
            throw new BusinessException("The uploaded file is not a valid image.");
        }
        Files.createDirectories(uploadDir());
        String name = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, uploadDir().resolve(name), StandardCopyOption.REPLACE_EXISTING);
        }
        return name;
    }

    private static boolean looksLikeImage(byte[] b, int n) {
        if (n >= 3 && (b[0] & 0xFF) == 0xFF && (b[1] & 0xFF) == 0xD8 && (b[2] & 0xFF) == 0xFF) {
            return true; // jpeg
        }
        if (n >= 8 && (b[0] & 0xFF) == 0x89 && b[1] == 'P' && b[2] == 'N' && b[3] == 'G') {
            return true; // png
        }
        return n >= 6 && b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8'; // gif
    }

    /** Removes an old upload (ignored when the name is empty or unsafe). */
    public static void deleteQuietly(String fileName) {
        if (fileName == null || fileName.isBlank() || fileName.contains("/") || fileName.contains("\\") || fileName.contains("..")) {
            return;
        }
        try {
            Files.deleteIfExists(uploadDir().resolve(fileName));
        } catch (IOException ignored) {
            // an undeletable old picture must never break the request
        }
    }
}
