package com.canaryshop.canaryshop.controllers;
import com.canaryshop.canaryshop.services.PageHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.services.ProductService;

@Controller
public class IndexManager {
    @Autowired
    private ProductService productService;
    @Autowired
    private PageHandler pageHandler;

    @GetMapping({"/", "/index"})
    public String index(Model model, @RequestParam(required = false) String search,@PageableDefault(size=12) Pageable page) {

        Page<Product> results;
        results= productService.getPageProducts(search, search, page);
        if (search!=null){
            model.addAttribute("search", search);
        }
        pageHandler.handleProductPage(model, results, page);
        return "index";
    }
}
