package com.example.sneaker_sophia.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleInternalServerError(Exception ex, Model model, HttpServletRequest request) {
        // Log lỗi
        logger.error("Internal Server Error occurred:", ex);

        // Cung cấp thông tin lỗi cho view
        System.out.println("check");
        System.out.println(request.getRequestURI());
        if(request.getRequestURI().contains("staff") || request.getRequestURI().contains("admin")){
            return "error5ad";
        }
        model.addAttribute("errorMessage", "Internal Server Error");

        return "/website/productwebsite/500"; // Trả về tên view/template cho lỗi 500
    }

}