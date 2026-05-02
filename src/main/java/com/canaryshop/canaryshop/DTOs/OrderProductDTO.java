package com.canaryshop.canaryshop.DTOs;

public record OrderProductDTO(
                Long id,
                int quantity,
                ProductSummaryDTO product) {
}
