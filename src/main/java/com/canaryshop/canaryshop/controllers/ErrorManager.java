package com.canaryshop.canaryshop.controllers;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorManager implements ErrorController{

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (statusCode == null){
            return "redirect:/";
        }
        HttpStatus status = HttpStatus.valueOf(statusCode);
        String attribute = switch (status){
            case BAD_REQUEST -> "error400";
            case NOT_FOUND -> "error404";
            case INTERNAL_SERVER_ERROR -> "error500";
            default -> "errorDefault";
        };
        model.addAttribute(attribute, true);
        return "error";
    }
    
}