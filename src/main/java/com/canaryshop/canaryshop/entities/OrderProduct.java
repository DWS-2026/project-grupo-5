package com.canaryshop.canaryshop.entities;
import jakarta.persistence.*;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @ManyToOne
    private Product product;
    @ManyToOne 
    private Order order;
    protected OrderProduct(){}
    public OrderProduct(Order order, Product product, int quantity){
        this.order = order;
        this.product = product;
        this.quantity = quantity;
    }
    public int getQuantity(){
        return this.quantity;
    }
    public Product getProduct(){
        return this.product;
    }
}
