package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

@Entity
public class OrderProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity; // The quiantity of the product in a order
    @ManyToOne
    private Product product;
    @ManyToOne
    private Order order;

    protected OrderProduct() {
    }

    public OrderProduct(Order order, Product product, int quantity) {
        this.order = order;
        this.product = product;
        this.setQuantity(quantity);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        int q = Math.min(quantity, this.product.getStock()); // The quiantity of the product in a order
        this.quantity = q;
    }

    public boolean canAddQuantity() {
        return this.quantity != product.getStock();
    }

    public Product getProduct() {
        return this.product;
    }

    public Order getOrder() {
        return order;
    }
}