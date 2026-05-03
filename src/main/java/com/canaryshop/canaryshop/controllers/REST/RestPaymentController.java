package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.DTOs.OrderDTO;
import com.canaryshop.canaryshop.DTOs.OrderMapper;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.ProductService;
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
public class RestPaymentController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @Operation(summary = "Pay the cart")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The cart have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the cart", content = @Content)
    })
    @PostMapping("/cart")
    public ResponseEntity<OrderDTO> payCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails.getUsername());
        Order cart = user.getCart();
        cart = this.orderService.renewOrder(cart);
        if (cart.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        this.orderService.closeCart(user, 1f);
        return ResponseEntity.ok(orderMapper.toDTO(cart));
    }

    @Operation(summary = "Pay the cart with a code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The cart have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the cart", content = @Content)
    })
    @PostMapping("/cart/{code}")
    public ResponseEntity<OrderDTO> payCartWithCode(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String code) {
        User user = userService.getUser(userDetails.getUsername());
        Order cart = user.getCart();
        cart = this.orderService.renewOrder(cart);
        if (cart.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }
        this.orderService.closeCart(user, this.orderService.getDiscount(code));
        return ResponseEntity.ok(orderMapper.toDTO(cart));
    }

    @Operation(summary = "Pay a product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The product have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
    })
    @PostMapping("/{id}")
    public ResponseEntity<OrderDTO> payProduct(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long productID) {
        User user = userService.getUser(userDetails.getUsername());
        Product product = productService.getProduct(productID);
        Order temp = new Order();
        temp.setProductQuantity(product, 1);
        temp.setUser(user);
        this.orderService.closeOrder(user, temp, 1f);
        return ResponseEntity.ok(orderMapper.toDTO(temp));
    }

    @Operation(summary = "Pay a product with a code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The product have been paid", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
    })
    @PostMapping("/{id}/{code}")
    public ResponseEntity<OrderDTO> applyDiscount(@AuthenticationPrincipal UserDetails userDetails,
            @PathVariable long productID, @RequestParam String code) {
        User user = userService.getUser(userDetails.getUsername());
        Product product = productService.getProduct(productID);
        Order temp = new Order();
        temp.setProductQuantity(product, 1);
        temp.setUser(user);
        this.orderService.closeOrder(user, temp, this.orderService.getDiscount(code));
        return ResponseEntity.ok(orderMapper.toDTO(temp));
    }

}
