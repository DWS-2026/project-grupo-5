package com.canaryshop.canaryshop.services;

import com.canaryshop.canaryshop.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class PageHandler {
    public void handleProductPage(Model model, Page<Product> results, Pageable page){
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
    }
}
