package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    ProductSummaryDTO toSummaryDTO(Product product);
}
