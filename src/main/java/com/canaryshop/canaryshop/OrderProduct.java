package com.canaryshop.canaryshop;
import jakarta.persistence.*;

@Entity
public class OrderProduct {
    private int quantity;
    @ManyToOne
    private Product product;
    @ManyToOne 
    private Order order;    
    public OrderProduct(){
    }
    public int getQuantity(){
        return this.quantity;
    }
    public Product getProduct(){
        return this.product;
    }
}
