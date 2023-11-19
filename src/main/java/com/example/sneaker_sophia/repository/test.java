package com.example.sneaker_sophia.repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class test {
    public static void main(String[] args) {
        // Lấy ngày giờ hiện tại
        LocalDateTime now = LocalDateTime.now();

        // Định dạng ngày giờ theo múi giờ Việt Nam
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // In kết quả
        System.out.println("Ngày giờ theo múi giờ Việt Nam: " + formattedDateTime);
    }
}
