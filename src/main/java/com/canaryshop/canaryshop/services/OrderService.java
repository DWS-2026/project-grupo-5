package com.canaryshop.canaryshop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.repositories.OrderRepository;

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
}