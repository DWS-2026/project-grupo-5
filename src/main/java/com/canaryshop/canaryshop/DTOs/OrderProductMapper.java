package com.canaryshop.canaryshop.DTOs;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.canaryshop.canaryshop.entities.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {
    @Mapping(source = "order", target = "order")
    @Mapping(source = "product", target = "product")
    OrderProductDTO toDTO(OrderProduct op);

    OrderProductBasicDTO toBasicDTO(OrderProduct op);

    List<OrderProductBasicDTO> toDTOs(Collection<OrderProduct> orderProducts);

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    OrderProduct toDomain(OrderProductBasicDTO op);

}
