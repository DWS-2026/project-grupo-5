package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.User;

import java.util.Collection;
import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public abstract class UserMapper {
    @Autowired
    PasswordEncoder passwordEncoder;

    public abstract UserBasicDTO toBasicDTO(User user);
    public abstract List<UserBasicDTO> toDTOs(Collection<User> users);
    public abstract UserDTO toDTO(User user);
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(user.password()))")
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "roles", expression = "java({\"USER\"})")
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "reported", ignore = true)
    public abstract User toDomain(UserLoginDTO user);
    
}
