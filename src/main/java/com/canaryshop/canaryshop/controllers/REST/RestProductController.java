package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.ProductDTO;
import com.canaryshop.canaryshop.DTOs.ProductSummaryDTO;
import com.canaryshop.canaryshop.DTOs.ProductMapper;
import com.canaryshop.canaryshop.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class RestProductController {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductService productService;

    @Operation(summary = "Get products with an optional query")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Found products to display",
            content = @Content(
                mediaType = "application/json",
                array = @ArraySchema(
                    schema = @Schema(implementation = ProductSummaryDTO.class)
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Page number is out of bounds",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Could not find products with given query",
            content = @Content
        )
    })
    @GetMapping("/")
    public Page<ProductSummaryDTO> getProducts(@RequestParam(required = false) String search, @PageableDefault(size=12) Pageable page){
        return productService.getPageProducts(search, search, page).map(product -> mapper.toSummaryDTO(product));
    }
    @Operation(summary="Returns a product with a given id")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Found the product",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ProductDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product with given id could not be found",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable long id){
        return mapper.toDTO(productService.getProduct(id));
    }
}
