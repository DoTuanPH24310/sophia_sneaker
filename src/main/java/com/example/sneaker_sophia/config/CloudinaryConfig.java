package com.example.sneaker_sophia.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "deihbhsfj";
    private final String API_KEY = "496935891654143";
    private final String API_SECRET = "4sGmW6UQlUDHdo9qKkPe8JymMZo";
    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);

        return new Cloudinary(config);
    }
}