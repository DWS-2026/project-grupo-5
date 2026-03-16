package com.canaryshop.canaryshop.controllers;
import com.canaryshop.canaryshop.services.UserService;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import com.canaryshop.canaryshop.entities.Product;
import com.canaryshop.canaryshop.repositories.UserRepository;
import com.canaryshop.canaryshop.services.ProductService;
import com.canaryshop.canaryshop.entities.User;

@Controller
public class IndexManager {
    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/index"})
    public String index(Model model, @RequestParam(required = false) String search,@PageableDefault(size=12) Pageable page) {
        
        Page<Product> results;
        results= productService.getPageProducts(search, search, page);
        if (search!=null){
            model.addAttribute("search", search);
        }
        model.addAttribute("products", results);
        model.addAttribute("hasprev",results.hasPrevious());
        model.addAttribute("hasnext",results.hasNext());
        model.addAttribute("prev", page.getPageNumber()-1);
        model.addAttribute("next", page.getPageNumber()+1);
        model.addAttribute("nextPage", page.getPageNumber()+2);
        int lastPage=results.getTotalPages();
        if (lastPage>page.getPageNumber()+2){
            model.addAttribute("lastPage", lastPage);
            model.addAttribute("lastPageLink", lastPage-1);
        }
        if (page.getPageNumber()>1){
            model.addAttribute("less",true);
        }
        if (lastPage-3>page.getPageNumber()){
            model.addAttribute("more",true);
        }
        model.addAttribute("page",page.getPageNumber());
        return "index";
    }
}
