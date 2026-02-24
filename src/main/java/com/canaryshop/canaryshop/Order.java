package com.canaryshop.canaryshop;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;



@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price = 0F;
    
    

    @OneToMany(mappedBy = "carts")
    private List<OrderProduct> products = new LinkedList<>(); 

    public Order(){}
    public Long getId() {
        return id;
    }
    public double getPrice(){
        return price;
    }
    public List<OrderProduct> getProducts(){
        return products;
    }
    public int getProductQuantity(OrderProduct product){
        OrderProduct p= products.get(products.indexOf(product));
        return p.getQuantity();

    }
    public void addProduct(Product product){
        if (products.contains(product)) { return; }
        products.add(product);
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
