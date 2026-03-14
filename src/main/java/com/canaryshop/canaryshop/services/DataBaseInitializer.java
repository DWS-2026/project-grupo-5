package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.entities.Image;

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
        User admin = new User("Admin", "admin@canaryshop.com", passwordEncoder.encode("admin"),"USER","ADMIN");
        User user1 = new User("User1", "user1@canaryshop.com", passwordEncoder.encode("user1"),"USER");
        userService.addUser(admin);
        userService.addUser(user1);
        for (int i=1; i<=56;i++){
            Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
            if(i%4==1){
                Product product = new Product(admin, "Samsung A"+i, "Buena calidad", 4.00*i, 1, image);
                product.addReview(new Review(user1, "GOTY", 5, "Viva android"));
                productService.addProduct(product);
            }else if(i%4==2){
                Product product = new Product(admin, "Samsung A"+i, "Seminuevo", 3.00*i, 1, image);
                product.addReview(new Review(user1, "good", 4, "Viva android"));
                productService.addProduct(product);
            }else if(i%4==3){
                Product product = new Product(admin, "Samsung A"+i, "Usado", 2.00*i, 1, image);
                product.addReview(new Review(user1, "Ta bien", 3, "Viva android"));
                productService.addProduct(product);
            }else{
                Product product = new Product(admin, "Samsung A"+i, "Roto", 1.00*i, 1, image);
                product.addReview(new Review(user1, "Un poco malo", 2, "Viva android"));
                productService.addProduct(product);
            }

        }
        for (int i=1; i<=16;i++){
            Image image2 = imageService.createImage("src/main/resources/static/assets/logo2.png");
            if(i%4==1){
                Product product = new Product(admin, "Iphone"+i, "Buena calidad", 40.00*i, 1, image2);
                product.addReview(new Review(user1, "Ta bien", 3, "Hater de iphones", image2));
                productService.addProduct(product);
            }else if(i%4==2){
                Product product = new Product(admin, "Iphone "+i, "Seminuevo", 30.00*i, 1, image2);
                product.addReview(new Review(user1, "meh", 2, "Hater de iphones", image2));
                productService.addProduct(product);
            }else if(i%4==3){
                Product product = new Product(admin, "Iphone "+i, "Usado", 20.00*i, 1, image2);
                product.addReview(new Review(user1, "puff", 1, "Hater de iphones", image2));
                productService.addProduct(product);
            }else{
                Product product = new Product(admin, "Iphone "+i, "Roto", 10.00*i, 1, image2);
                product.addReview(new Review(user1, "Mierdon", 0, "Hater de iphones", image2));
                productService.addProduct(product);

            }
        }
    }
}
