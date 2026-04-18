package com.canaryshop.canaryshop.services;


import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    public Order getOrder(long id){
        return orderRepository.findById(id).orElseThrow();
    }

    public void addOrder(Order o){
        orderRepository.save(o);
    }
    // To get the discount for the code
    public float getDiscount(String code){
        float discount = switch(code == null ? "" : code){
            case "DiegoEsElMejor" -> 0;
            case "JaimeEsElMejor" -> 0.25f;
            case "VictorEsElMejor" -> 0.5f;
            case "JorgeEsElMejor" -> 0.75f;
            default -> 1f;
        };
        return discount;
    }
    // To create a new cart for the user
    public void closeCart(User u, float discount){
        this.closeOrder(u, u.getCart(), discount);
        Order newCart = new Order();
        u.setCart(newCart);
        this.orderRepository.save(newCart);
        this.userService.addUser(u);
    }
    // To close and order with the discount
    public void closeOrder(User u, Order order, float discount){
        order.closeOrder();
        order.setDiscount(discount);
        this.productsPurchased(order);
        u.addOrder(order);
        this.orderRepository.save(order);
        this.userService.addUser(u);
    }
    // To decrease the stock of the products
    private void productsPurchased(Order o){
        o.getProducts().forEach(op -> this.productService.productPurchased(op.getProduct()));
    }
    // Renew the order by removing the out-of-stock products
    public Order renewOrder(Order o){
        List<OrderProduct> products = new LinkedList<>(o.getProducts()); 
        for(OrderProduct op: products){
            Product p= productService.getProduct(op.getProduct().getId());
            if(!p.isAvailable()){
                o.setProductQuantity(op.getProduct(), 0);
            }
        }
        return this.orderRepository.save(o);
    }
}