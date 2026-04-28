package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productImages", target = "images")
    ProductDTO toDTO(Product product);

    ProductSummaryDTO toSummaryDTO(Product product);

    @Mapping(target = "vendor", source = "user")
    @Mapping(target = "productImages", ignore = true)
    @Mapping(target = "reported", ignore = true)
    Product toDomain(ProductUploadDTO product, User user);
}
