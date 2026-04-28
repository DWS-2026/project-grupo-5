package com.canaryshop.canaryshop.DTOs;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.canaryshop.canaryshop.entities.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {
    
    @Mapping(source = "product", target = "product")
    OrderProductsBasicDTO toBasicDTO(OrderProduct op);
    List<OrderProductsBasicDTO> toDTOs(Collection<OrderProduct> orderProducts);

}
