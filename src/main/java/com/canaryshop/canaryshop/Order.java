package com.canaryshop.canaryshop;

import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;


@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price = 0F;
    
    

    @ManyToMany(mappedBy = "carts")
    private final Map<Product, Integer> products = new HashMap<>();

    public Order(){}
    public Long getId() {
        return id;
    }
    public double getPrice(){
        return price;
    }
    public Set<Product> getProducts(){
        return products.keySet();
    }
    public int getProductQuantity(Product product){
        return this.products.get(product);
    }
    public void addProduct(Product product){
        if (products.containsKey(product)) { return; }
        products.put(product, 1);
        price+=product.getPrice();
    }
    public void removeProduct(Product product){
        if (!products.containsKey(product)){ return; }
        int amount = products.get(product);
        price-=product.getPrice()*amount;
        products.remove(product);
    }
    public void setAmount(Product product, int amount){
        amount = Math.min(amount, product.getStock());
        price-=product.getPrice()*products.get(product);
        price+=product.getPrice()*amount;
        products.replace(product, amount);
    }
}
