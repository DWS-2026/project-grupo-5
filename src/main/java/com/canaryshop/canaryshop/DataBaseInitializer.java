package com.canaryshop.canaryshop;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DataBaseInitializer {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductService productService;
    @Autowired
    private ImageService imageService;
    
    @PostConstruct
    public void initDatabase() {
        userService.addUser(new User("Admin", "admin@canaryshop.com", passwordEncoder.encode("admin"),"User","Admin"));
        userService.addUser(new User("User1", "user1@canaryshop.com", passwordEncoder.encode("user1"),"User"));
        Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
        Image image2 = imageService.createImage("src/main/resources/static/assets/logo2.png");
        Product product = new Product("Samsung A56", "Buena calidad", 300.50, 1, image);
        product.addReview(new Review("test", 5, "TEST"));
        product.addReview(new Review("test", 5, "test 2", image2));
        productService.addProduct(product);
    }
}
