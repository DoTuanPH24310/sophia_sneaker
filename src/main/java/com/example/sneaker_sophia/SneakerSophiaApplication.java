package com.example.sneaker_sophia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SneakerSophiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerSophiaApplication.class, args);
    }

}
