package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.SoluongService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    private CartService cartService;
    @Autowired
    private SoluongService soluongService;

    @PostMapping("/{gioHangId}/{chiTietGiayId}/increase")
    public void increaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        soluongService.increaseQuantity(gioHangId, chiTietGiayId);
    }

    @PostMapping("/{gioHangId}/{chiTietGiayId}/decrease")
    public void decreaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        soluongService.decreaseQuantity(gioHangId, chiTietGiayId);
    }

    @GetMapping("/laydulieumoi")
    public Map<String, Object> getUpdatedData() {
        Map<String, Object> response = new HashMap<>();
        String email = "tuan@gmail.com";
        // Lấy dữ liệu mới từ service
        List<GioHangChiTiet> products = this.soluongService.getUpdatedProducts();
        long cartItemsCount = this.cartService.countCartItems(email);

        response.put("products", products);
        response.put("cartItemsCount", cartItemsCount);

        return response;
    }
}

