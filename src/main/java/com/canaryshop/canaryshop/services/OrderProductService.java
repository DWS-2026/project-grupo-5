package com.canaryshop.canaryshop.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.repositories.OrderProductRepository;

@Service
public class OrderProductService {
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private OrderService orderService;

    public List<OrderProduct> getAllOrderProducts() {
        return orderProductRepository.findAll();
    }

    public Page<OrderProduct> getPageOrderProduct(Pageable pageable) {
        return this.orderProductRepository.findAll(pageable);
    }

    public OrderProduct getOrderProductById(Long id) {
        return orderProductRepository.findById(id).orElseThrow();
    }

    public Page<OrderProduct> getPageOrderProductsByOrder(long id, Pageable pageable) {
        Order o = this.orderService.getOrder(id);
        return this.orderProductRepository.findByOrder(o, pageable);
    }
}
