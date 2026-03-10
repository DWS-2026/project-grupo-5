package com.canaryshop.canaryshop.services;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.UserRepository;

@Service
public class DataBaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostConstruct
    public void init() throws IOException {
        userRepository.save(new User("user", "user@canaryshop.com", passwordEncoder.encode("password")));

    }
}
