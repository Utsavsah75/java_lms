package com.library.listener;

import com.library.util.DBConnection;
import com.library.util.FileStorage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/** Start-up: creates the upload folder, copies the sample book covers once, and checks the database connection. */
@WebListener
public class AppInitListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppInitListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        Path uploads = FileStorage.uploadDir();
        try {
            Files.createDirectories(uploads);
            String samples = ctx.getRealPath("/WEB-INF/sample-uploads");
            if (samples != null && Files.isDirectory(Path.of(samples))) {
                try (Stream<Path> files = Files.list(Path.of(samples))) {
                    files.filter(Files::isRegularFile).forEach(f -> {
                        Path target = uploads.resolve(f.getFileName().toString());
                        if (!Files.exists(target)) {
                            try {
                                Files.copy(f, target, StandardCopyOption.COPY_ATTRIBUTES);
                            } catch (IOException e) {
                                LOG.log(Level.WARNING, "Could not copy sample image " + f, e);
                            }
                        }
                    });
                }
            }
            LOG.info("Upload folder: " + uploads);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot prepare upload folder " + uploads, e);
        }
        try (Connection c = DBConnection.getConnection()) {
            LOG.info("Database connection OK: " + c.getMetaData().getURL());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "DATABASE NOT REACHABLE - check db.properties and that MySQL is running", e);
        }
    }
}
