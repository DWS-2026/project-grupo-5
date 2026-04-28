package com.canaryshop.canaryshop.DTOs;

public record UserLoginDTO(
        String username,
        String email,
        String password
) {}
