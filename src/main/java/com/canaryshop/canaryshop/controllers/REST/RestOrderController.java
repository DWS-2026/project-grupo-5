package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.canaryshop.canaryshop.DTOs.*;

import com.canaryshop.canaryshop.services.OrderProductService;
import com.canaryshop.canaryshop.services.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/orders")
public class RestOrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderProductService orderProductService;

    @GetMapping("/")
    public Page<OrderBasicDTO> getAllOrders(Pageable pageable) {
        return this.orderService.getPageOrders(pageable).map(orderMapper::toBasicDTO);
    }

    @GetMapping("/{id}")
    public OrderDTO getOrder(@PathVariable long id) {

        return orderMapper.toDTO(this.orderService.getOrder(id));
    }

    @GetMapping("/{id}/user")
    public UserDTO getOrderUser(@PathVariable long id) {
        return userMapper.toDTO(this.orderService.getOrder(id).getUser());
    }

    @GetMapping("/{id}/products")
    public Page<OrderProductBasicDTO> getOrderProductsByOrder(@PathVariable long id, Pageable pageable) {
        return this.orderProductService.getPageOrderProductsByOrder(id, pageable).map(orderProductMapper::toBasicDTO);
    }

}
