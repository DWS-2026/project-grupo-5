package com.canaryshop.canaryshop.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.repositories.OrderRepository;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository or;

    public Order getOrder(long id){
        Optional<Order> order = or.findById(id);
        if (order.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        return order.get();
    }

    public void addOrder(Order o){
        or.save(o);
    }
}