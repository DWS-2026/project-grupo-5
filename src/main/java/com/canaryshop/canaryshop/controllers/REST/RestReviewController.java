package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.Review;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/products")
public class RestReviewController {
    @Autowired
    private ProductService products;
    @Autowired
    private ReviewService reviews;
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService users;

    @Operation(summary = "Get reviews for a given product id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found reviews in product",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ReviewDTO.class)
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Either could not find product with given id, or the product has no reviews",
                    content = @Content
            )
    })
    @GetMapping("/{pid}/reviews")
    public Collection<ReviewDTO> getReviews(@PathVariable long pid){
        return reviewMapper.toDTOs(reviews.getReviews(pid));
    }

    @Operation(summary="Returns a specific review within a given product")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Found the review",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product or review could not be found",
                    content = @Content
            )
    })
    @GetMapping("/{pid}/reviews/{rid}")
    public ReviewDTO getReviews(@PathVariable long pid, @PathVariable long rid){
        return reviewMapper.toDTO(reviews.getReview(rid));
    }
    @PostMapping("/{id}/reviews")
    public ResponseEntity<ReviewDTO> addReview(Principal principal, @PathVariable long id, @RequestBody ReviewUploadDTO review){
        User user = users.getUser(principal);
        Product product = products.getProduct(id);
        Review entityReview = reviewMapper.toDomain(review, user);
        reviews.createReview(entityReview, product);
        entityReview = product.getReviews().getLast();
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entityReview.getId()).toUri();
        return ResponseEntity.created(path).body(reviewMapper.toDTO(entityReview));
    }
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> editReview(Principal principal, @PathVariable long productId, @PathVariable long reviewId, @RequestBody ReviewUploadDTO modification){
        User user = users.getUser(principal);
        Review entityReview = reviews.getReview(reviewId);
        reviews.editReview(user, entityReview, reviewMapper.toDomain(modification, user));
        return ResponseEntity.ok().body(reviewMapper.toDTO(entityReview));
    }
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(Principal principal, @PathVariable long productId, @PathVariable long reviewId){
        User user = users.getUser(principal);
        Review entityReview = reviews.getReview(reviewId);
        reviews.deleteReview(user, entityReview);
        return ResponseEntity.ok().body(reviewMapper.toDTO(entityReview));
    }
}
