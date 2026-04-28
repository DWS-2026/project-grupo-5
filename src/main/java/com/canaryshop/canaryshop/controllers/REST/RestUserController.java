package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.UserBasicDTO;
import com.canaryshop.canaryshop.DTOs.UserDTO;
import com.canaryshop.canaryshop.DTOs.UserLoginDTO;
import com.canaryshop.canaryshop.DTOs.UserMapper;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    @Autowired
    UserService users;
    @Autowired
    UserMapper userMapper;

    @PostMapping("/")
    public ResponseEntity<UserBasicDTO> register(@RequestBody UserLoginDTO user){
        users.addUser(userMapper.toDomain(user));
        User entityUser = users.getUser(user.username());
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entityUser.getId()).toUri();
        return ResponseEntity.created(path).body(userMapper.toBasicDTO(entityUser));
    }
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return userMapper.toDTO(users.findById(id));
    }
    @GetMapping("/")
    public Page<UserBasicDTO> getAllUsers(Pageable pageable) {
        return users.getPageUser(null,pageable).map(userMapper::toBasicDTO);
    }
}
    
