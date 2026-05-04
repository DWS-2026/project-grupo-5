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
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.Collection;

@RestController
@Tag(name="Reviews", description = "Endpoints related to manipulating product reviews")
@RequestMapping("/api/v1/products")
public class RestReviewController {
    @Autowired
    private ProductService products;
    @Autowired
    private ReviewService reviews;
    @Autowired
    private ReviewMapper reviewMapper;
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

    @Operation(summary = "Creates a new review for a product")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Review was created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Review contents are invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User is not logged in or cannot edit review",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product with given ID was not found",
                    content = @Content
            )
    })
    @PreAuthorize("isAuthenticated()")
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

    @Operation(summary = "Edits a product review")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was edited successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Edited review contents are invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User is not logged in or cannot edit review",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product or review with given ID was not found",
                    content = @Content
            )
    })
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> editReview(Principal principal, @PathVariable long productId, @PathVariable long reviewId, @RequestBody ReviewUploadDTO modification){
        User user = users.getUser(principal);
        Review entityReview = reviews.getReview(reviewId);
        reviews.editReview(user, entityReview, reviewMapper.toDomain(modification, user));
        return ResponseEntity.ok().body(reviewMapper.toDTO(entityReview));
    }

    @Operation(summary = "Deletes a product review")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Review was deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReviewDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User is not logged in or cannot delete review",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product or review with given ID was not found",
                    content = @Content
            )
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{productId}/reviews/{reviewId}")
    public ResponseEntity<ReviewDTO> deleteReview(Principal principal, @PathVariable long productId, @PathVariable long reviewId){
        User user = users.getUser(principal);
        Review entityReview = reviews.getReview(reviewId);
        reviews.deleteReview(user, entityReview);
        return ResponseEntity.ok().body(reviewMapper.toDTO(entityReview));
    }
}
