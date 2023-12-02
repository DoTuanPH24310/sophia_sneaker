package com.example.sneaker_sophia.controller.RestController;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.SoLuongSessionService;
import com.example.sneaker_sophia.validate.AlertInfo;
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
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private SoLuongSessionService soLuongSessionService;
    @GetMapping("/{chiTietGiayId}/increase")
    public ResponseEntity<String> increaseQuantity(@PathVariable UUID chiTietGiayId, HttpSession session) {
        try {
            if (!soLuongSessionService.canIncreaseQuantity(chiTietGiayId, session)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Số lượng giỏ hàng vượt quá số lượng sản phẩm.");
            }
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
            // Kiểm tra số lượng mới có hợp lệ không
            if (newQuantity <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: Số lượng không hợp lệ");
            }

            // Lấy số lượng sản phẩm từ dịch vụ
            Integer productQuantity = soLuongSessionService.getProductQuantity(chiTietGiayId);

            // Kiểm tra số lượng mới không lớn hơn số lượng sản phẩm
            if (productQuantity != null && newQuantity > productQuantity) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request: Số lượng giỏ hàng lớn hơn số lượng sản phẩm");
            }

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
            alertInfo.alert("errOnline", "Số lượng không hợp lệ");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        }
    }

    @GetMapping("/removeAll")
    public ResponseEntity<String> removeAllItems(HttpSession session) {
        try {
            soLuongSessionService.removeAllItems(session);
            return ResponseEntity.ok("All items removed successfully");
        } catch (EntityNotFoundException e) {
            alertInfo.alert("errOnline", "Số lượng không hợp lệ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        }
    }



}




