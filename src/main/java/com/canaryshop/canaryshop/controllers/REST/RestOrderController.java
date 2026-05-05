package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.entities.Order;
import com.canaryshop.canaryshop.entities.OrderProduct;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.OrderService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

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
        private ProductService productService;
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
                        @ApiResponse(responseCode = "200", description = "Found the products from the order", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderProductDTO.class)))),
                        @ApiResponse(responseCode = "404", description = "Could not found any products by the ID from the order", content = @Content)
        })
        @GetMapping("/{id}/products")
        public Page<OrderProductDTO> getOrderProductsByOrder(@AuthenticationPrincipal UserDetails userDetails,
                        @PathVariable long id, Pageable pageable) {
                User u = userService.getUser(userDetails.getUsername());
                return this.orderService.getPageOrderProductsByOrder(id, pageable, u)
                                .map(orderProductMapper::toDTO);
        }

        @Operation(summary = "Get the cart from an user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Found the cart of the user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Could not found the user", content = @Content)
        })
        @GetMapping("/cart")
        public OrderDTO getCart(@AuthenticationPrincipal UserDetails userDetails) {
                User u = userService.getUser(userDetails.getUsername());
                return orderMapper.toDTO(u.getCart());
        }

        @Operation(summary = "Add a product to the cart")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product have been uploaded", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderProductDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @PostMapping("/cart/increase/{id}")
        public ResponseEntity<OrderProductDTO> addProductToCart(@PathVariable long id,
                        @AuthenticationPrincipal UserDetails userDetails) {
                User u = userService.getUser(userDetails.getUsername());
                Order cart = u.getCart();
                Product product = productService.getProduct(id);
        
                if (cart.getProductQuantity(product) + 1 > product.getStock()) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock available for this product");
                }

                cart.setProductQuantity(product, cart.getProductQuantity(product) + 1);
                orderService.addOrder(cart);
                userService.addUser(u);
                OrderProduct ret = orderService.getOrderProductByProductAndCart(product, cart);
                URI location = fromCurrentRequest().path("/{id}").buildAndExpand(ret.getId()).toUri();

                return ResponseEntity.created(location).body(orderProductMapper.toDTO(ret));
        }

        @Operation(summary = "Decrease a product from the cart")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product have been decreased", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderProductDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @PostMapping("/cart/decrease/{id}")
        public ResponseEntity<OrderProductDTO> decreaseProductToCart(@PathVariable long id,
                        @AuthenticationPrincipal UserDetails userDetails) {
                User u = userService.getUser(userDetails.getUsername());
                Order cart = u.getCart();
                Product product = productService.getProduct(id);
                if (cart.getProductQuantity(product) <= 1) {
                        cart.setProductQuantity(product, 0);
                        orderService.addOrder(cart);
                        userService.addUser(u);
                        
                        return ResponseEntity.noContent().build(); 
                } else {
                        cart.setProductQuantity(product, cart.getProductQuantity(product) - 1);
                        orderService.addOrder(cart);
                        userService.addUser(u);
                        
                        OrderProduct ret = orderService.getOrderProductByProductAndCart(product, cart);
                        return ResponseEntity.ok(orderProductMapper.toDTO(ret));
                }
        }

        @Operation(summary = "Delete a product from the cart")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Product have been deleted from the cart", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderProductDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Could not found the product", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @DeleteMapping("/cart/{id}")
        public ResponseEntity<Void> removeProductFromCart(@PathVariable long id,
                        @AuthenticationPrincipal UserDetails userDetails) {
                User u = userService.getUser(userDetails.getUsername());
                Order cart = u.getCart();
                Product product = productService.getProduct(id);
                cart.setProductQuantity(product, 0);
                orderService.addOrder(cart);
                userService.addUser(u);

                return ResponseEntity.noContent().build();
        }

        @Operation(summary = "Clear all the cart")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "The cart has been cleared", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
                        @ApiResponse(responseCode = "404", description = "Could not found the cart", content = @Content)
        })
        @PreAuthorize("isAuthenticated()")
        @DeleteMapping("/cart")
        public ResponseEntity<OrderDTO> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
                User u = userService.getUser(userDetails.getUsername());
                Order cart = u.getCart();
                this.orderService.clearOder(cart);
                userService.addUser(u);
                return ResponseEntity.ok(orderMapper.toDTO(u.getCart()));
        }

}
