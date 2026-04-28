package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.ProductDTO;
import com.canaryshop.canaryshop.DTOs.ProductMapper;
import com.canaryshop.canaryshop.DTOs.ReviewDTO;
import com.canaryshop.canaryshop.DTOs.ReviewMapper;
import com.canaryshop.canaryshop.DTOs.UserBasicDTO;
import com.canaryshop.canaryshop.DTOs.UserDTO;
import com.canaryshop.canaryshop.DTOs.UserLoginDTO;
import com.canaryshop.canaryshop.DTOs.UserMapper;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.ReviewService;
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


@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    @Autowired
    UserService users;
    @Autowired
    UserMapper userMapper;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductService productService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    ReviewMapper reviewMapper;

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
    @GetMapping("/{id}/products")
    public Page<ProductDTO> getProductsByUser(@PathVariable long id, Pageable pageable) {

        return productService.getProductsByVendor(id, pageable).map(productMapper::toDTO);
    }
    @GetMapping("/{id}/reviews")
    public Page<ReviewDTO> getReviewsByUser(@PathVariable long id, Pageable pageable) {
        User u = this.users.findById(id);
        return this.reviewService.getReviewsByAuthor(u).map(reviewMapper::toDTO);
    }
    
    
}
    
