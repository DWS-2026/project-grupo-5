package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.ReviewService;
import com.canaryshop.canaryshop.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    @Autowired
    private UserService users;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;

    @PostMapping("/")
    public ResponseEntity<UserBasicDTO> register(@RequestBody UserLoginDTO user) {
        users.addUser(userMapper.toDomain(user));
        User entityUser = users.getUser(user.email());
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entityUser.getId())
                .toUri();
        return ResponseEntity.created(path).body(userMapper.toBasicDTO(entityUser));
    }

    @Operation(summary = "Get an user from the ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicDTO.class))),
            @ApiResponse(responseCode = "404", description = "Could not found any user with the ID given", content = @Content)
    })
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable long id) {
        return userMapper.toDTO(users.findById(id));
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserBasicDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Could not found any user", content = @Content)
    })
    @GetMapping("/")
    public Page<UserBasicDTO> getAllUsers(Pageable pageable) {
        return users.getPageUser(null, pageable).map(userMapper::toBasicDTO);
    }

    @Operation(summary = "Get all products from an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found products from an user", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Either could not find products from the user id, or the user has no products", content = @Content)
    })
    @GetMapping("/{id}/products")
    public Page<ProductDTO> getProductsByUser(@PathVariable long id, Pageable pageable) {

        return productService.getProductsByVendor(id, pageable).map(productMapper::toDTO);
    }

    @Operation(summary = "Get all reviews from an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found reviews from an user", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ReviewDTO.class)))),
            @ApiResponse(responseCode = "404", description = "Either no reviews could be found for the user ID, or the user has not uploaded any reviews", content = @Content)
    })
    @GetMapping("/{id}/reviews")
    public Page<ReviewDTO> getReviewsByUser(@PathVariable long id, Pageable pageable) {
        User u = this.users.findById(id);
        return this.reviewService.getReviewsByAuthor(u, pageable).map(reviewMapper::toDTO);
    }

    @Operation(summary = "Delete an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been deleted", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "404", description = "There is no user with the given ID", content = @Content),
            @ApiResponse(responseCode = "403", description = "The user does not have the required permissions to delete this user", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        User currentUser = users.getUser(userDetails.getUsername());
        User user = users.findById(id);
        this.users.deleteUser(currentUser, user);
        return ResponseEntity.ok(userMapper.toDTO(user));
    }

    @Operation(summary = "Update an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User has been updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserBasicDTO.class))),
            @ApiResponse(responseCode = "404", description = "There is no user with the given ID", content = @Content),
            @ApiResponse(responseCode = "403", description = "You do not have permission to update the user", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid request body", content = @Content)
    })
    @PutMapping("/")
    public ResponseEntity<UserBasicDTO> putMethodName(@RequestBody UserBasicDTO user,
            @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = this.users.getUser(userDetails.getUsername());
        User u = this.userMapper.toDomainID(user);
        this.users.updateUser(currentUser, u);
        return ResponseEntity.ok(user);
    }
    @PostMapping("/{id}/{report}")
    public ResponseEntity <UserBasicDTO> postMethodName(@PathVariable long id,@PathVariable String report) {
        User user = users.findById(id);
        user.report(report);
        return ResponseEntity.ok(userMapper.toBasicDTO(user));
    }
    @GetMapping("/reports")
    public Page<UserReportDTO> getMethodName(@AuthenticationPrincipal UserDetails ud, Pageable pageable) {
        User user = this.users.getUser(ud.getUsername());
        if(!user.getRoles().contains("ADMIN")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not admin");
        }
        return users.getReportedUser(null,pageable).map(userMapper::toReportUserDTO);
    }
}
