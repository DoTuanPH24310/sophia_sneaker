package com.example.sneaker_sophia.controller.RestController;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.service.SoLuongSessionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/giohangchitiet/session")
public class SoLuongSessionRest {

    @Autowired
    private SoLuongSessionService soLuongSessionService;
    @GetMapping("/{chiTietGiayId}/increase")
    public ResponseEntity<String> increaseQuantity(@PathVariable UUID chiTietGiayId, HttpSession session) {
        try {
            soLuongSessionService.increaseQuantity(chiTietGiayId, session);
            return ResponseEntity.ok("Quantity increased successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        }
    }

    @GetMapping("/{chiTietGiayId}/decrease")
    public ResponseEntity<String> decreaseQuantity(@PathVariable UUID chiTietGiayId, HttpSession session) {
        try {
            soLuongSessionService.decreaseQuantity(chiTietGiayId, session);
            return ResponseEntity.ok("Quantity decreased successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        }
    }

    @GetMapping("/{chiTietGiayId}/updatequantity")
    public ResponseEntity<String> updateQuantity(@PathVariable UUID chiTietGiayId, @RequestParam int newQuantity, HttpSession session) {
        try {
            soLuongSessionService.updateQuantity(chiTietGiayId, newQuantity, session);
            return ResponseEntity.ok("Quantity updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: " + e.getMessage());
        }
    }

    @GetMapping("/{chiTietGiayId}/remove")
    public ResponseEntity<String> removeItem(@PathVariable UUID chiTietGiayId, HttpSession session) {
        try {
            soLuongSessionService.removeItem(chiTietGiayId, session);
            return ResponseEntity.ok("Item removed successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        }
    }

}




