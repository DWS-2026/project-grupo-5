package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record UserDTO(
        Long id,
        String username,
        String email,
        ImageDTO image,
        List<ProductSummaryDTO> products) {
}
