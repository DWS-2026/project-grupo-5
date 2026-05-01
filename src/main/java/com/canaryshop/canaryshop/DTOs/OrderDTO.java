package com.canaryshop.canaryshop.DTOs;

import java.util.List;

public record OrderDTO(
        Long id,
        double price,
        UserBasicDTO user,
        boolean isClosed,
        List<OrderProductBasicDTO> products) {
}
