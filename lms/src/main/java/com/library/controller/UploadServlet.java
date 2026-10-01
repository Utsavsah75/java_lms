package com.library.controller;

import com.library.util.FileStorage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/** Serves book covers and student photos (the PHP project linked straight to uploads/). */
@WebServlet(name = "UploadServlet", urlPatterns = "/uploads/*")
public class UploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getPathInfo();
        if (name == null || name.length() < 2) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        name = name.substring(1);
        String lower = name.toLowerCase(Locale.ROOT);
        String type = lower.endsWith(".png") ? "image/png"
                : lower.endsWith(".gif") ? "image/gif"
                : (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) ? "image/jpeg" : null;
        if (type == null || name.contains("/") || name.contains("\\") || name.contains("..")) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path dir = FileStorage.uploadDir();
        Path file = dir.resolve(name).normalize();
        if (!file.startsWith(dir) || !Files.isRegularFile(file)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(type);
        resp.setContentLengthLong(Files.size(file));
        resp.setHeader("Cache-Control", "public, max-age=3600");
        resp.setHeader("X-Content-Type-Options", "nosniff");
        Files.copy(file, resp.getOutputStream());
    }
}
