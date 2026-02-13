package com.canaryshop.canaryshop;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorManage implements ErrorController{

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null){ 
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.BAD_REQUEST.value()){
                model.addAttribute("error400", true);
            } else if (statusCode == HttpStatus.NOT_FOUND.value()){
                model.addAttribute("error404", true);
            }else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){
                model.addAttribute("error500", true);
            }else {
                model.addAttribute("errorDefault", true);
            }
        }

        return "error";
    }
    
    
}