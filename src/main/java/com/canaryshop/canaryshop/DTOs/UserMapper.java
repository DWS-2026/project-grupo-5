package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    PasswordEncoder passwordEncoder;

    public abstract UserBasicDTO toBasicDTO(User user);
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(user.password()))")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "roles", expression = "java({\"USER\"})")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "reported", ignore = true)
    public abstract User toDomain(UserLoginDTO user);
}
