package com.canaryshop.canaryshop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class LoginManager {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "iniciosesion";
    }

    @PostMapping("/login")
    public String postMethodName(@RequestBody String email, @RequestBody String password, Model model) {
        var userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(password)) {
            return "redirect:/index";
        }else {
            model.addAttribute("showLogin", true);
            model.addAttribute("showRegister", false);
            model.addAttribute("error", "Invalid email or password");
            return "redirect:/login";
        }
    }
    

    @GetMapping("/register")
    public String getMethodName(Model model) {
        model.addAttribute("showRegister", true);
        model.addAttribute("showLogin", false);
        return "iniciosesion";
    }

    @PostMapping("/register")
    public String postMethodName(@RequestBody String username, @RequestBody String email, @RequestBody String password, Model model) {
        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("showRegister", true);
            model.addAttribute("showLogin", false);
            model.addAttribute("error", "Email already in use");
            return "redirect:/register";
        }

        User entity = new User(username, email, password);
        userRepository.save(entity);
        model.addAttribute("showLogin", true);
        model.addAttribute("showRegister", false);
        return "redirect:/login";
    }
    


    
}
