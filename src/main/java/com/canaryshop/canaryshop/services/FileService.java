package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
        this.rootLocation = Paths.get(uploadDir);
    }

    private String sanitizeName(String fileName){
        return fileName.replace("/", "_")
                .replace("\\", "_")
                .replace("..", "");
    }

    public String storeFile(MultipartFile file) {
        if (file==null || file.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            String sanitizedName = sanitizeName(Objects.requireNonNull(file.getOriginalFilename()));
            Path destination = this.rootLocation.resolve(Paths.get(sanitizedName));
            if (!destination.getParent().equals(this.rootLocation)) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }
            Files.createDirectories(rootLocation);
            InputStream fileInputStream = file.getInputStream();
            if (Files.exists(destination)){
                sanitizedName = "(%s) ".formatted(UUID.randomUUID()) + sanitizedName;
                destination = this.rootLocation.resolve(Paths.get(sanitizedName));
            }
            Files.copy(fileInputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            return sanitizedName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public Resource loadFile(String fileName) {
        String sanitizedName = sanitizeName(fileName);
        try {
            Path file = rootLocation.resolve(sanitizedName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new NoSuchElementException();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public void deleteFile(String fileName) {
        String sanitizedName = sanitizeName(fileName);
        try {
            Path file = rootLocation.resolve(sanitizedName);
            if (!Files.deleteIfExists(file)){
                throw new NoSuchElementException();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file: " + e.getMessage());
        }
    }
}