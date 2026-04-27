package com.canaryshop.canaryshop.DTOs;

import com.canaryshop.canaryshop.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserBasicDTO toBasicDTO(User user);
}
