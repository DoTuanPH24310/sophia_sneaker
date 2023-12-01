package com.example.sneaker_sophia.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex) {
        // Log lỗi
        logger.error("Error occurred:", ex);

        // Xử lý logic khi có ngoại lệ
        return "error"; // Trả về tên view/template
    }
    // Các phương thức khác xử lý các loại ngoại lệ khác (nếu cần)
}