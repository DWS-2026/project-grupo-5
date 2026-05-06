package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    private final Path rootLocation;

    public FileService(@Value("${file.upload-dir}") String uploadDir) {
        // Normalize the base path at startup to ensure consistent comparisons
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Resolves the file path and validates that it remains within the root directory.
     * This prevents Path Traversal attacks.
     */
    private Path getSafePath(String fileName) {
        Path destinationFile = this.rootLocation.resolve(Paths.get(fileName))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation)) {
            throw new RuntimeException("Security breach attempt: Cannot access files outside the target directory.");
        }
        return destinationFile;
    }

    public String storeFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        try {
            String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            
            // 1. Extract and validate extension
            String extension = "";
            int i = originalName.lastIndexOf('.');
            if (i > 0) {
                extension = originalName.substring(i).toLowerCase();
            }

            // Optional: Security whitelist for extensions
            if (!extension.matches("\\.(jpg|jpeg|png|gif|pdf)")) {
                throw new RuntimeException("File type not allowed.");
            }

            // 2. Generate a unique name to prevent collisions and enumeration attacks
            String newFileName = UUID.randomUUID().toString() + extension;
            Path destinationFile = getSafePath(newFileName);

            // 3. Create directories if they don't exist
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            // 4. Save the file using try-with-resources to ensure the stream is closed
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file", e);
        }
    }

    public Resource loadFile(String fileName) {
        try {
            Path file = getSafePath(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NoSuchElementException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error resolving file URL", e);
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path file = getSafePath(fileName);
            if (!Files.deleteIfExists(file)) {
                throw new NoSuchElementException("File does not exist for deletion.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while deleting file: " + e.getMessage());
        }
    }
}