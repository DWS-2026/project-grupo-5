package com.canaryshop.canaryshop.entities;

import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;


@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double price = 0F;
    private boolean isClosed = false;
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderProduct> products = new LinkedList<>();

    public Order(){}
    public Long getId() {
        return id;
    }
    public double getPrice(){
        double total = 0;
        for (OrderProduct op : products) {
            total += op.getProduct().getPrice() * op.getQuantity();
        }
        return total;
    }
    
    public List<OrderProduct> getProducts(){
        return products;
    }
    private OrderProduct findOrderProductByProduct(Product product){
        for (OrderProduct op: products){
            if (op.getProduct().equals(product)) {
                return op;
            }
        }
        return null;
    }
    public void closeOrder(){
        isClosed = true;
    }
    public int getProductQuantity(Product product){
        Optional<OrderProduct> op = Optional.ofNullable(findOrderProductByProduct(product));
        if (op.isEmpty()) {
            return 0;
        }
        return op.get().getQuantity();
    }
    public void addProduct(OrderProduct op){
        products.add(op);
    }
    public void removeProduct(OrderProduct op){
        products.remove(op);
    }
    public void setUser(User user){
        this.user = user;
    }
}
