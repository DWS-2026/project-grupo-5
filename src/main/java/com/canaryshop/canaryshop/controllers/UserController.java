package com.canaryshop.canaryshop.controllers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.canaryshop.canaryshop.entities.Image;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.services.ImageService;
import com.canaryshop.canaryshop.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/user/{id}")
    public String getUser(Model model, @PathVariable long id) {
        model.addAttribute("user",this.userService.findById(id));
        List<Product> products = this.userService.getProductsByVendor(id);
        List<Product> only5= new LinkedList<>();
        int limite = Math.min(5, products.size()); 
        for (int i = 0; i < limite; i++) {
            only5.add(products.get(i));
        }
        model.addAttribute("products",only5);
        return "user";
    }
    @GetMapping("/user/{id}/products")
    public String getAllUserProducts(Model model, @PathVariable long id, @PageableDefault(size=12) Pageable page) {

        List<Product> allProducts = this.userService.getProductsByVendor(id);
        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), allProducts.size());
    
        List<Product> pageContent;
        if (start >= allProducts.size()) {
            pageContent = List.of(); 
        } else {
            pageContent = allProducts.subList(start, end);
        }

        Page<Product> results = new PageImpl<>(pageContent, page, allProducts.size());
        model.addAttribute("products", results.getContent());
        model.addAttribute("hasprev", results.hasPrevious());
        model.addAttribute("hasnext", results.hasNext());
        model.addAttribute("prev", page.getPageNumber() - 1);
        model.addAttribute("next", page.getPageNumber() + 1);
        model.addAttribute("nextPage", page.getPageNumber() + 2);
    
        int lastPage = results.getTotalPages();
        if (lastPage > page.getPageNumber() + 2){
            model.addAttribute("lastPage", lastPage);
            model.addAttribute("lastPageLink", lastPage - 1);
        }
        if (page.getPageNumber() > 1){
            model.addAttribute("less", true);
        }
        if (lastPage - 3 > page.getPageNumber()){
            model.addAttribute("more", true);
        }
        model.addAttribute("page", page.getPageNumber());
        model.addAttribute("id",this.userService.findById(id).getId());
        return "userProducts";
    }
    @PostMapping("/user/{id}/image")
    public String postMethodName(Model model, @RequestBody MultipartFile image, @PathVariable long id) {
        Image img= this.imageService.createImage(image);
        this.userService.findById(id).setImage(img);
        return this.getUser(model, id);
    }
    
    
}
