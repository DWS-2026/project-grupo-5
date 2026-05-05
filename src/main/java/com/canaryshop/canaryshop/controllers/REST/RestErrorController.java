package com.canaryshop.canaryshop.controllers.REST;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RestErrorController implements ErrorController {
    @RequestMapping(value="/error", produces = "application/json")
    public void handleError(HttpServletRequest request) {
        return;
    }
    
}
