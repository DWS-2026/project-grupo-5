package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

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
    private boolean isAdmin = false;

    @OneToMany(mappedBy="vendor", cascade=CascadeType.ALL)
    private final List<Product> products = new LinkedList<>();
    @OneToOne(cascade=CascadeType.ALL)
    private Order cart;
    @OneToMany(cascade=CascadeType.ALL)
    private final List<Order> orders = new LinkedList<>();
    @OneToOne(cascade=CascadeType.ALL)
    private Image image;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Empty constructor for JPA
    protected User(){}

    public String getUsername() {
        return username;
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
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
