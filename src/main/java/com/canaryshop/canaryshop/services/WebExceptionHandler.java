package com.canaryshop.canaryshop.services;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class WebExceptionHandler{
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public Object handleNotFound(HttpServletRequest request){
        return redirect(request, HttpStatus.NOT_FOUND);
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Object handleBadRequest(HttpServletRequest request){
        return redirect(request, HttpStatus.BAD_REQUEST);
    }
    private Object redirect(HttpServletRequest request, HttpStatus status){
        if (request.getRequestURI().contains("/api/v1/")){
            return null;
        }
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, status.value());
        return "forward:/error";
    }
}
