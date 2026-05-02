package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.services.OrderProductService;

@RestController
@RequestMapping("/api/v1/orderProducts")
public class RestOrderProductController {
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderProductMapper orderProductMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;

    @GetMapping("/{id}")
    public OrderProductDTO getOrderProduct(@PathVariable long id) {
        return this.orderProductMapper.toDTO(this.orderProductService.getOrderProductById(id));
    }

    @GetMapping("/")
    public Page<OrderProductBasicDTO> getAllOrderProducts(Pageable pageable) {
        return this.orderProductService.getPageOrderProduct(pageable).map(orderProductMapper::toBasicDTO);
    }

    @GetMapping("/{id}/order")
    public OrderDTO getOrderByOrderProduct(@PathVariable long id) {
        return this.orderMapper.toDTO(this.orderProductService.getOrderProductById(id).getOrder());
    }

    @GetMapping("/{id}/product")
    public ProductDTO getProductByOrderProduct(@PathVariable long id) {
        return productMapper.toDTO(this.orderProductService.getOrderProductById(id).getProduct());
    }

}
