package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.SoluongService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/giohangchitiet")
public class GioHangChiTietController {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private SoluongService soluongService;

    @Autowired
    private GioHangRepository gioHangRepository;

    @GetMapping("/{gioHangId}/{chiTietGiayId}/increase")
    public void increaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        soluongService.increaseQuantity(gioHangId, chiTietGiayId);
    }

    @GetMapping("/{gioHangId}/{chiTietGiayId}/decrease")
    public void decreaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        soluongService.decreaseQuantity(gioHangId, chiTietGiayId);
    }

    @GetMapping("/{gioHangId}/{chiTietGiayId}/updatequantity")
    public ResponseEntity<String> updateQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId, @RequestParam int newQuantity) {
        try {
            this.soluongService.updateQuantity(gioHangId, chiTietGiayId, newQuantity);
            return new ResponseEntity<>("Cập nhật số lượng thành công", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi cập nhật số lượng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}

