package com.example.sneaker_sophia.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class ImageUploadController {

    @GetMapping("/getimage/{photo}")
    public ResponseEntity<ByteArrayResource> getImage(@PathVariable("photo") String photo) {
        if (photo != null) {
            try {
                Path filename = Paths.get("uploads", photo);
                byte[] buffer = Files.readAllBytes(filename);
                ByteArrayResource byteArrayResource = new ByteArrayResource(buffer);
                return ResponseEntity.ok()
                        .contentLength(buffer.length)
                        .contentType(MediaType.parseMediaType("image/jpg"))
                        .body(byteArrayResource);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }
}
