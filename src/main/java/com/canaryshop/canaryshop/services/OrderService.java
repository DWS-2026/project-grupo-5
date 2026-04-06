package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.repositories.OrderProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderProductRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    public Order getOrder(long id){
        Optional<Order> order = orderProductRepository.findById(id);
        if (order.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order.get();
    }

    public void addOrder(Order o){
        orderProductRepository.save(o);
    }

    public float getDiscount(String code){
        float discount = switch(code == null ? "" :code){
            case "DiegoEsElMejor" -> 0;
            case "JaimeEsElMejor" -> 0.25f;
            case "VictorEsElMejor" -> 0.5f;
            case "JorgeEsElMejor" -> 0.75f;
            default -> 1f;
        };
        return discount;
    }

    public void closeCart(User u){
        Order cart = u.getCart();
        this.productsPurchased(cart);
        cart.closeOrder();
        u.addOrder(cart);
        Order newCart = new Order();
        u.setCart(newCart);
        this.orderRepository.save(cart);
        this.orderRepository.save(newCart);
        this.userService.addUser(u);
    }

    public void closeOrder(User u, Order order){
        order.closeOrder();
        this.productsPurchased(order);
        u.addOrder(order);
        this.orderRepository.save(order);
        this.userService.addUser(u);
    }

    private void productsPurchased(Order o){
        List<OrderProduct> list= o.getProducts();

        for (OrderProduct op : list){
            this.productService.productPurchased(op.getProduct());
        }
    }
}