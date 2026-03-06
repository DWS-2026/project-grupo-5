package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.entities.User;


@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;

    public void addUser(User user){
        this.repo.save(user);
    }
}
