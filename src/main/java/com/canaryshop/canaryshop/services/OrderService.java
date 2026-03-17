package com.canaryshop.canaryshop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;
import com.canaryshop.canaryshop.repositories.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository or;
    @Autowired
    private OrderProductRepository opr;

    public OrderProduct getOrderProduct(long id) throws ResponseStatusException {
        Optional<OrderProduct> request = opr.findById(id);
        if (request.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return request.get();
    }
    public void addOrder(Order o){
        or.save(o);
    }
    public void setAmount(OrderProduct op,int amount){
        op.setQuantity(amount);
        opr.save(op);
    }
    public void addOrderProduct(OrderProduct op){
        opr.save(op);
    }
}