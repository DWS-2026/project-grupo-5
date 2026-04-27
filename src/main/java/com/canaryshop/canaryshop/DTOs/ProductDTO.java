package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record ProductDTO(
    long id,
    String name,
    UserBasicDTO vendor,
    Double price,
    Integer stock,
    String description,
    List<ReviewSummaryDTO> reviews
) {}
