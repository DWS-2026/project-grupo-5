package com.canaryshop.canaryshop.services;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class WebExceptionHandler{
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound(HttpServletRequest request){
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 404);
        return "forward:/error";
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(HttpServletRequest request){
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 400);
        return "forward:/error";
    }
}
