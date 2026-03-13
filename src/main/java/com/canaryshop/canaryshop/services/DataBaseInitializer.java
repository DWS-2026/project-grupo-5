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
        userService.addUser(new User("Admin", "admin@canaryshop.com", passwordEncoder.encode("admin"),"USER","ADMIN"));
        userService.addUser(new User("User1", "user1@canaryshop.com", passwordEncoder.encode("user1"),"USER"));
        for (int i=1; i<=56;i++){
            Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
            if(i%4==1){
                Product product = new Product("Samsung A"+i, "Buena calidad", 4.00*i, 1, image);
                product.addReview(new Review("GOTY", 5, "Viva android"));
                productService.addProduct(product);
            }else if(i%4==2){
                Product product = new Product("Samsung A"+i, "Seminuevo", 3.00*i, 1, image);
                product.addReview(new Review("good", 4, "Viva android"));
                productService.addProduct(product);
            }else if(i%4==3){
                Product product = new Product("Samsung A"+i, "Usado", 2.00*i, 1, image);
                product.addReview(new Review("Ta bien", 3, "Viva android"));
                productService.addProduct(product);
            }else{
                Product product = new Product("Samsung A"+i, "Roto", 1.00*i, 1, image);
                product.addReview(new Review("Un poco malo", 2, "Viva android"));
                productService.addProduct(product);
            }
            
        }
        for (int i=1; i<=16;i++){
            Image image2 = imageService.createImage("src/main/resources/static/assets/logo2.png");
            if(i%4==1){
                Product product = new Product("Iphone"+i, "Buena calidad", 40.00*i, 1, image2);
                product.addReview(new Review("Ta bien", 3, "Hater de iphones", image2));
                productService.addProduct(product);
            }else if(i%4==2){
                Product product = new Product("Iphone "+i, "Seminuevo", 30.00*i, 1, image2);
                product.addReview(new Review("meh", 2, "Hater de iphones", image2));
                productService.addProduct(product);
            }else if(i%4==3){
                Product product = new Product("Iphone "+i, "Usado", 20.00*i, 1, image2);
                product.addReview(new Review("puff", 1, "Hater de iphones", image2));
                productService.addProduct(product);
            }else{
                Product product = new Product("Iphone "+i, "Roto", 10.00*i, 1, image2);
                product.addReview(new Review("Mierdon", 0, "Hater de iphones", image2));
                productService.addProduct(product);

            }
        }
    }
}
