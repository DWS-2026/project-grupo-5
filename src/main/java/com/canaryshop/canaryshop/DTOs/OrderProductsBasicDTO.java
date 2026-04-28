package com.canaryshop.canaryshop.DTOs;


public record OrderProductsBasicDTO(
    Long id,
    int quantity,
    ProductSummaryDTO product
) {}
