package com.canaryshop.canaryshop.DTOs;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.canaryshop.canaryshop.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    @Mapping(source = "products", target = "orderProducts")
    OrderBasicDTO toBasicDTO(Order o);

    List<OrderBasicDTO> toDTOs(Collection<Order> orders);
    
}
