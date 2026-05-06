package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.StringDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.DTOs.OrderDTO;
import com.canaryshop.canaryshop.DTOs.OrderMapper;
import com.canaryshop.canaryshop.DTOs.StringDTO;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/payment")
@PreAuthorize("isAuthenticated()")
public class RestPaymentController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;

    
    @Operation(summary = "Pay the cart with or without a code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The cart have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the cart", content = @Content)
    })
    @PostMapping("/cart")
    public ResponseEntity<OrderDTO> payCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody(required = false) StringDTO code) {
        User user = userService.getUser(userDetails.getUsername());
        Order cart = this.orderService.createTempCart(user, null);
        if (cart.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        String discountCode = (code != null) ? code.str() : null;
        this.orderService.closeOrder(user, cart,this.orderService.getDiscount(discountCode));
        return ResponseEntity.ok(orderMapper.toDTO(cart));
    }

    @Operation(summary = "Pay a product with or without a code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The product have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
    })
    @PostMapping("/{productID}")
    public ResponseEntity<OrderDTO> payProduct(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long productID, @RequestBody(required = false) StringDTO code) {
        User user = userService.getUser(userDetails.getUsername());
        Order temp = this.orderService.createTempCart(user, productID);
        String discountCode = (code != null) ? code.str() : null;
        this.orderService.closeOrder(user, temp, this.orderService.getDiscount(discountCode));
        return ResponseEntity.ok(orderMapper.toDTO(temp));
    }
}
