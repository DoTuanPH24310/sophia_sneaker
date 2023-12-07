package com.example.sneaker_sophia.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request) {

        System.out.println("check: ");
        System.out.println(request.getRequestURI());
        logger.error("Error occurred:", ex);

        return "error"; // Trả về tên view/template
    }
    // Các phương thức khác xử lý các loại ngoại lệ khác (nếu cần)
}