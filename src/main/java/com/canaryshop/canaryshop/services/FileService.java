package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

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
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Archivo no válido o vacío.");
        }

        try {
            String rawName = file.getOriginalFilename();
            if (rawName == null) throw new RuntimeException("Nombre de archivo nulo.");
            
            String originalName = StringUtils.cleanPath(rawName);
            
            if (originalName.contains("..") || originalName.contains("/") || originalName.contains("\\")) {
                throw new RuntimeException();
            }

            String extension = "";
            int lastDot = originalName.lastIndexOf('.');
            if (lastDot > 0) {
                extension = originalName.substring(lastDot).toLowerCase();
            }

            List<String> allowedExtensions = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".pdf");
            if (!allowedExtensions.contains(extension)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported file type. Allowed types: " + String.join(", ", allowedExtensions));
            }

            String contentType = file.getContentType();
            if (contentType == null || !isSupportedContentType(contentType)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported content type. Allowed types: " + String.join(", ", Arrays.asList("image/jpeg", "image/png", "image/gif", "application/pdf")));
            }

            Path destinationFile = this.rootLocation.resolve(originalName).normalize().toAbsolutePath();
            
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Security breach attempt: Cannot access files outside the target directory.");
            }

            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            
            return originalName;

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while storing file: " + e.getMessage());
        }
    }

    private boolean isSupportedContentType(String contentType) {
        return Arrays.asList("image/jpeg", "image/png", "image/gif", "application/pdf").contains(contentType);
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