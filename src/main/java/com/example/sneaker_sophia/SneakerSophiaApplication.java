package com.example.sneaker_sophia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Random;

@SpringBootApplication
@EnableScheduling

public class SneakerSophiaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SneakerSophiaApplication.class, args);
    }

//    public static void main(String[] args) {
//        String numbers = "0123456789";
//        Random random = new Random();
//        StringBuilder otp = new StringBuilder(6);
//
//        for (int i = 0; i < 6; i++) {
//            int index = random.nextInt(numbers.length());
//            otp.append(numbers.charAt(index));
//        }
//
//        System.out.println(otp);
//    }



    // Hàm tạo mã OTP



}
