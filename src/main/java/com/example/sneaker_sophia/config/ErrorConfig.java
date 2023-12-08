//package com.example.sneaker_sophia.config;
//
//import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.Collections;
//
//@Configuration
//public class ErrorConfig {
//
//    @Bean
//    public ErrorViewResolver customErrorViewResolver() {
//        return (request, status, model) -> {
//            if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
//                // Nếu là lỗi 500, trả về trang lỗi tùy chỉnh
//                return new ModelAndView("error");
//            }
//            // Các trường hợp lỗi khác, để Spring xử lý theo cách mặc định
//            return null;
//        };
//    }
//}