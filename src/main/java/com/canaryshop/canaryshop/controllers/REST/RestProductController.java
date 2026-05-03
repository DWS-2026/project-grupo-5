package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.*;
import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.entities.User;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name="Products", description = "Endpoints related to manipulating products")
public class RestProductController {
    @Autowired
    private ProductMapper mapper;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService users;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageMapper imageMapper;

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
    public Page<ProductSummaryDTO> getProducts(
            @Parameter(description = "Optional query")
            @RequestParam(required = false) String search, @PageableDefault(size=12) @ParameterObject Pageable page){
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

    @Operation(summary = "Upload a product to the page")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product was created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid object was passed",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "The session token is not present or has expired",
                    content = @Content
            )
    })
    @PostMapping("/")
    public ResponseEntity<ProductDTO> uploadProduct(Principal principal, @RequestBody ProductUploadDTO product){
        User user = users.getUser(principal);
        Product entityProduct = mapper.toDomain(product, user);
        productService.addProduct(entityProduct);
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(entityProduct.getId()).toUri();
        return ResponseEntity.created(path).body(mapper.toDTO(entityProduct));
    }
    @Operation(summary = "Modify a product")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Object passed is invalid",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to edit the product",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product with id was not found",
                    content = @Content
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> editProduct(Principal principal, @PathVariable long id, @RequestBody ProductUploadDTO modification){
        User user = users.getUser(principal);
        Product product = productService.getProduct(id);
        Product entityProductModified = mapper.toDomain(modification, user);
        productService.editProduct(user, product, entityProductModified);
        return ResponseEntity.ok(mapper.toDTO(product));
    }
    @Operation(summary = "Delete a product with a given id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to delete product",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product was not found",
                    content = @Content
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(Principal principal, @PathVariable long id){
        User user = users.getUser(principal);
        Product product = productService.getProduct(id);
        productService.deleteProduct(user, product);
        return ResponseEntity.ok(mapper.toDTO(product));
    }

    @GetMapping("/{id}/images")
    public List<ImageDTO> getProductImages(@PathVariable long id){
        return imageMapper.toDTOs(productService.getProduct(id).getProductImages());
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<ImageDTO> uploadImage(Principal principal, @PathVariable long id, @RequestParam MultipartFile imageFile){
        Product product = productService.getProduct(id);
        User user = users.getUser(principal);
        Image image = imageService.createImage(imageFile);
        productService.addImage(user, product, image);
        image = product.getProductImages().getLast();
        URI path = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(image.getId()).toUri();
        return ResponseEntity.created(path).body(imageMapper.toDTO(image));
    }

    @PutMapping("/{productId}/images/{imageId}")
    public ResponseEntity<Object> editProductImage(Principal principal, @PathVariable long productId, @PathVariable long imageId, @RequestParam MultipartFile imageFile){
        Product product = productService.getProduct(productId);
        User user = users.getUser(principal);
        productService.modifyCheck(user, product);
        Image image = imageService.getImageEntity(imageId);
        imageService.replaceImage(image, imageFile);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}/images/{imageId}")
    public ResponseEntity<ImageDTO> deleteProductImage(Principal principal, @PathVariable long productId, @PathVariable long imageId){
        Product product = productService.getProduct(productId);
        User user = users.getUser(principal);
        productService.modifyCheck(user, product);
        Image image = imageService.getImageEntity(imageId);
        productService.deleteImage(user, product, image);
        return ResponseEntity.ok().body(imageMapper.toDTO(image));
    }
    @Operation(summary = "Get a product from the ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was successfully reported",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductSummaryDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product was not found",
                    content = @Content
            )
    })
     @PostMapping("/{id}/{report}")
    public ResponseEntity<ProductSummaryDTO> postMethodName(@PathVariable long id,@PathVariable String report) {
        Product product = productService.getProduct(id);
        product.report(report);
        return ResponseEntity.ok(mapper.toSummaryDTO(product));
    }
    @Operation(summary = "Get reported products")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Reports were successfully earned",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                            schema = @Schema(implementation = ProductReportDTO.class)
                        )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permission to get the reports",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reported products were not found",
                    content = @Content
            )
    })
    @GetMapping("/reports")
    public Page<ProductReportDTO> getMethodName(@AuthenticationPrincipal UserDetails ud, Pageable pageable) {
        User user = this.users.getUser(ud.getUsername());
        if(!user.getRoles().contains("ADMIN")){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not admin");
        }
        return productService.getReportedProducts(null,null,pageable).map(mapper::toReportProductDTO);
    }
}
