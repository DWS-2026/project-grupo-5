package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private List<String> roles;

    @OneToMany(mappedBy="vendor", cascade=CascadeType.ALL)
    private final List<Product> products = new LinkedList<>();
    @OneToOne(cascade=CascadeType.ALL)
    private Order cart;
    @OneToMany(cascade=CascadeType.ALL, mappedBy = "user")
    private final List<Order> orders = new LinkedList<>();
    @OneToOne(cascade=CascadeType.ALL)
    private Image image;

    public User(String username, String email, String password, String ... roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = List.of(roles);
        this.cart = new Order();
        this.cart.setUser(this);
    }

    // Empty constructor for JPA
    protected User(){}

    public String getUsername() {
        return username;
    }
    public boolean isAdmin(){
        return roles.contains("ADMIN");
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Image getImage() {
        return image;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Order getCart() {
        return cart;
    }
    public void setCart(Order cart) {
        this.cart = cart;
    }
    public List<Order> getOrders() {
        return orders;
    }
    public Long getId() {
        return id;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void addProduct(Product product){
        this.products.add(product);
    }
    public List<String> getRoles() {
        return roles;
    }
    public void setRoles(String roles) {
        this.roles.add(roles); 
    }
    public void addOrder(Order order){
        order.setUser(this);
        this.orders.add(order);
    }
    public boolean canEdit(User user){
        return user != null && (
                this.id.equals(user.id) ||
                user.isAdmin()
        );
    }
    public float getRating(){
        float rating = 0;
        int count = 0;
        for (Product product: products) {
            if (product.getRating() == 0){ continue; }
            rating += product.getRating();
            count++;
        }
        if (count == 0) {
            return 0;
        }
        return rating / count;
    }
    public String getFormattedRating(){
        float rating = getRating();
        BigDecimal format = BigDecimal.valueOf(rating);
        return format.setScale(2, RoundingMode.HALF_UP).toString();
    }
}