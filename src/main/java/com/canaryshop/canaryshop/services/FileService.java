package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

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
        this.rootLocation = Paths.get(uploadDir);
    }

public String storeFile(MultipartFile file) {

    if (file.isEmpty()) {
        throw new RuntimeException("Failed to store empty file.");
    }

    try {

        String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int i = originalName.lastIndexOf('.');
        if (i > 0) {
            extension = originalName.substring(i);
        }
        
        String newFileName = UUID.randomUUID().toString() + extension;

        Path destinationFile = this.rootLocation.resolve(Paths.get(newFileName))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new RuntimeException("Cannot store file outside current directory.");
        }

        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }
        
        return newFileName;

    } catch (IOException e) {
        throw new RuntimeException("Failed to store file", e);
    }
}

    public Resource loadFile(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        try {
            Path file = rootLocation.resolve(fileName);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
    }
}