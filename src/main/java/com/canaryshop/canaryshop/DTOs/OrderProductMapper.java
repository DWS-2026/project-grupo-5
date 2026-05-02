package com.canaryshop.canaryshop.DTOs;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.canaryshop.canaryshop.entities.OrderProduct;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    @Mapping(source = "product", target = "product")
    OrderProductDTO toDTO(OrderProduct op);

    List<OrderProductDTO> toDTOs(Collection<OrderProduct> orderProducts);

    @Mapping(target = "order", ignore = true)
    OrderProduct toDomain(OrderProductDTO op);

}
