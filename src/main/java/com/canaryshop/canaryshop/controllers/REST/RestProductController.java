package com.canaryshop.canaryshop.controllers.REST;

import com.canaryshop.canaryshop.DTOs.ProductDTO;
import com.canaryshop.canaryshop.DTOs.ProductSummaryDTO;
import com.canaryshop.canaryshop.DTOs.ProductMapper;
import com.canaryshop.canaryshop.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class RestProductController {
    @Autowired
    ProductMapper mapper;
    @Autowired
    private ProductService productService;

    @GetMapping({"", "/"})
    public Page<ProductSummaryDTO> getProducts(@RequestParam(required = false) String search, @PageableDefault(size=12) Pageable page){
        return productService.getPageProducts(search, search, page).map(product -> mapper.toSummaryDTO(product));
    }
    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable long id){
        return mapper.toDTO(productService.getProduct(id));
    }
}
