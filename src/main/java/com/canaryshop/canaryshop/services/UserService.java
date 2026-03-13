package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.entities.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@Service
public class UserService {
    
    @Autowired
    private UserRepository repo;

    public void addUser(User user){
        this.repo.save(user);
    }
    public User getUser(String email){
        Optional<User> user = repo.findByEmail(email);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return user.get();
    }
    
}
