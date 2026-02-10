package com.canaryshop.canaryshop;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
public class IndexManage {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Hello, World!");
        return "index";
    }
    
}
