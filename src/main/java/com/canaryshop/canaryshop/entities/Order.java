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
    

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<OrderProduct> products = new LinkedList<>();

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
    public void addProduct(Product product){
        Optional<OrderProduct> existing = Optional.ofNullable(findOrderProductByProduct(product));
        if (existing.isPresent()){
            return;
        }
        OrderProduct orderProduct = new OrderProduct(this, product, 1);
        products.add(orderProduct);
        price+=product.getPrice();
    }
    public void removeProduct(Product product){
        Optional<OrderProduct> existing = Optional.ofNullable(findOrderProductByProduct(product));
        if (existing.isEmpty()){
            return;
        }
        OrderProduct toBeRemoved = existing.get();
        int amount = toBeRemoved.getQuantity();
        price-=product.getPrice()*amount;
        products.remove(toBeRemoved);
    }
    public void setAmount(Product product, int amount){
        amount = Math.min(amount, product.getStock());
        Optional<OrderProduct> existing = Optional.ofNullable(findOrderProductByProduct(product));
        if (existing.isPresent()){
            price-=product.getPrice()*existing.get().getQuantity();
            products.remove(existing.get());
        }
        price+=product.getPrice()*amount;
        products.add(new OrderProduct(this, product, amount));
    }
}
