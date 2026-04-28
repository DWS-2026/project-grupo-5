package com.canaryshop.canaryshop.DTOs;

public record ProductUploadDTO(
        String name,
        String description,
        double price,
        int stock
) {}
