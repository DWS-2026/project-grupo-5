package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record ReviewDTO(
        long id,
        String title,
        String description,
        int rating,
        UserBasicDTO author,
        List<String> files
) {}
