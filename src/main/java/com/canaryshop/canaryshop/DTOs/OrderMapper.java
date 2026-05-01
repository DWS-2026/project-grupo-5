package com.canaryshop.canaryshop.DTOs;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.canaryshop.canaryshop.entities.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "products", target = "products")
    OrderDTO toDTO(Order o);

    OrderBasicDTO toBasicDTO(Order o);

    List<OrderBasicDTO> toDTOs(Collection<Order> orders);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "isClosed", ignore = true)
    Order toDomain(OrderBasicDTO o);
}
