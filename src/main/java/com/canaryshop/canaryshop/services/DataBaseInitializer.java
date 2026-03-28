package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Order;

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
            admin.setImage(imageService.createImage("src/main/resources/static/assets/logo2.png"));
            User user1 = new User("User1", "user1@canaryshop.com", passwordEncoder.encode("user1"),"USER");
            userService.addUser(admin);
            userService.addUser(user1);
            for (int i=1; i<=56;i++){
                Product product = null;
                switch(i%4){
                    case 0 -> {
                        Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
                        product = new Product(admin, "Samsung A"+i, "Buena calidad", 4.00*i, 1, image);
                        product.addReview(new Review(user1, "GOTY", 5, "Viva android"));
                    }
                    case 1 -> {
                        Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
                        product = new Product(admin, "Samsung A"+i, "Seminuevo", 3.00*i, 1, image);
                        product.addReview(new Review(user1, "good", 4, "Viva android"));
                    }
                    case 2 -> {
                        Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
                        product = new Product(admin, "Samsung A"+i, "Usado", 2.00*i, 1, image);
                        product.addReview(new Review(user1, "Ta bien", 3, "Viva android"));
                    }
                    case 3 -> {
                        Image image = imageService.createImage("src/main/resources/static/assets/logo.png");
                        product = new Product(user1, "Samsung A"+i, "Roto", 1.00*i, 1, image);
                        product.addReview(new Review(admin, "Un poco malo", 2, "Viva android"));
                        product.report("mierdon");
                    }
                };
                productService.addProduct(product);
            }
            Order order = user1.getCart();
            for (int i=1; i<=16;i++){
                Product product = null;
                 Image image2 = imageService.createImage("src/main/resources/static/assets/logo2.png");
                    Image image3 = imageService.createImage("src/main/resources/static/assets/user.jpg");
                switch(i%4){
                    case 0 -> {
                        product = new Product(admin, "Iphone"+i, "Buena calidad", 40.00*i, 12, image2);
                        product.addReview(new Review(user1, "Ta bien", 3, "Hater de iphones", image3));
                        order.setProductQuantity(product, 1);
                    }
                    case 1 -> {

                        product = new Product(admin, "Iphone "+i, "Seminuevo", 30.00*i, 1, image2);
                        product.addReview(new Review(user1, "meh", 2, "Hater de iphones", image3));
                    }
                    case 2 -> {

                        product = new Product(admin, "Iphone "+i, "Usado", 20.00*i, 1, image2);
                        product.addReview(new Review(user1, "puff", 1, "Hater de iphones", image3));
                    }
                    case 3 -> {

                        product = new Product(user1, "Iphone "+i, "Roto", 10.00*i, 1, image2);
                        product.addReview(new Review(admin, "Mierdon", 0, "Hater de iphones", image3));
                    }
                }
                productService.addProduct(product);
            }
            this.userService.addUser(user1);
            Order o = new Order();
            for (int i=0;i<10;i++){
                Image img = imageService.createImage("src/main/resources/static/assets/user.jpg");
                Product pruebaOrder = new Product(user1, "Memoria RAM "+i, "de coleccion", 2000.00, 2, img);
                productService.addProduct(pruebaOrder);
                o.setProductQuantity(pruebaOrder, 1);
            }
            o.closeOrder();
            admin.addOrder(o);
            userService.addUser(admin);
    }
}