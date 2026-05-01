package com.canaryshop.canaryshop.entities;

import com.canaryshop.canaryshop.services.NumberFormatter;
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
    private List<OrderProduct> products = new LinkedList<>();

    public Order() {
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public Long getId() {
        return id;
    }

    public double getPrice() {
        return this.price;
    }

    public String getFormattedPrice() {
        return NumberFormatter.getFormattedNumber(this.price);
    }

    public List<OrderProduct> getProducts() {
        return products;
    }

    private Optional<OrderProduct> getOrderProduct(Product product) {
        OrderProduct found = null;
        for (OrderProduct op : products) {
            if (op.getProduct().equals(product)) {
                found = op;
                break;
            }
        }
        return Optional.ofNullable(found);
    }

    public void closeOrder() {
        this.isClosed = true;
    }

    public int getProductQuantity(Product product) {
        Optional<OrderProduct> op = this.getOrderProduct(product);
        if (op.isEmpty()) {
            return 0;
        }
        return op.get().getQuantity();
    }

    public boolean owns(User user) {
        return !(user == null) && (this.user.equals(user) || user.isAdmin());
    }

    // To set the amount of a product
    public void setProductQuantity(Product product, int amount) {
        Optional<OrderProduct> op = this.getOrderProduct(product);
        if (op.isEmpty()) {
            if (amount > 0) {
                this.addProduct(new OrderProduct(this, product, amount)); // Add the product in case doesn't exists and
                                                                          // have amount
            }
            return;
        }
        OrderProduct orderProduct = op.get();
        this.removeProduct(orderProduct);
        if (amount < 1) {
            return;
        }
        orderProduct.setQuantity(amount);
        this.addProduct(orderProduct);
    }

    // Add the product
    private void addProduct(OrderProduct op) {
        products.add(op);
        this.price += op.getProduct().getPrice() * op.getQuantity();
    }

    // Remove the product
    private void removeProduct(OrderProduct op) {
        products.remove(op);
        this.price -= op.getProduct().getPrice() * op.getQuantity();
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setDiscount(float discount) {
        this.price *= discount;
    }
}
