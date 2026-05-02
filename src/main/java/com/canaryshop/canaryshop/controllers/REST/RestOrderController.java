package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.OrderProductService;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all orders for an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found orders for the user", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderBasicDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Could not found any orders for the user", content = @Content)
    })
    @GetMapping("/")
    public Page<OrderBasicDTO> getAllOrders(@AuthenticationPrincipal UserDetails userDetails, Pageable pageable) {
        User u = userService.getUser(userDetails.getUsername());
        return this.orderService.getPageOrders(pageable, u).map(orderMapper::toBasicDTO);
    }

    @Operation(summary = "Get an order by its ID for an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found order by its ID for the user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found any orders by the ID for the user", content = @Content)
    })
    @GetMapping("/{id}")
    public OrderDTO getOrder(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        User u = userService.findByEmail(userDetails.getUsername());
        return orderMapper.toDTO(this.orderService.getOrder(id, u));
    }

    @Operation(summary = "Get the products from an order by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the products from the order", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderProductBasicDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Could not found any products by the ID from the order", content = @Content)
    })
    @GetMapping("/{id}/products")
    public Page<OrderProductBasicDTO> getOrderProductsByOrder(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long id, Pageable pageable) {
        User u = userService.getUser(userDetails.getUsername());
        return this.orderProductService.getPageOrderProductsByOrder(id, pageable, u)
                .map(orderProductMapper::toBasicDTO);
    }

}
