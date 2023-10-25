package com.example.sneaker_sophia.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CORSConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.setAllowCredentials(true); // Cho phép truy cập với thông tin đăng nhập
        config.addAllowedOriginPattern("*"); // Cho phép truy cập từ mọi domain
        config.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP
        config.addAllowedHeader("*"); // Cho phép tất cả các loại header
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}