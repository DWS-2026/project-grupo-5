package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productImages", target = "images")
    ProductDTO toDTO(Product product);
    ProductSummaryDTO toSummaryDTO(Product product);
}
