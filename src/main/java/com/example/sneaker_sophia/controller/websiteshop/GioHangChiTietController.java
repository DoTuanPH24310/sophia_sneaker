package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.SoluongService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private LoginRepository loginRepository;
    @Autowired
    private SoluongService soluongService;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @GetMapping("/{gioHangId}/{chiTietGiayId}/increase")
    public ResponseEntity<String> increaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        try {
            // Kiểm tra và thực hiện tăng số lượng từ service
            String result = soluongService.increaseQuantity(gioHangId, chiTietGiayId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Trả về thông báo lỗi nếu có lỗi
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi khi tăng số lượng: " + e.getMessage());
        }
    }


    @GetMapping("/{gioHangId}/{chiTietGiayId}/decrease")
    public ResponseEntity<String> decreaseQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId) {
        try {
            boolean result = soluongService.decreaseQuantity(gioHangId, chiTietGiayId);
            if (result) {
                return new ResponseEntity<>("Số lượng giảm thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Số lượng không đủ để giảm", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi giảm số lượng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{gioHangId}/{chiTietGiayId}/updatequantity")
    public ResponseEntity<String> updateQuantity(@PathVariable UUID gioHangId, @PathVariable UUID chiTietGiayId, @RequestParam int newQuantity) {
        try {
            boolean isValidQuantity = soluongService.isValidQuantity(chiTietGiayId, newQuantity);
            if (isValidQuantity) {
                soluongService.updateQuantity(gioHangId, chiTietGiayId, newQuantity);
                return new ResponseEntity<>("Cập nhật số lượng thành công", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Số lượng không hợp lệ: " + newQuantity, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi khi cập nhật số lượng: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/check-quantity/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkProductQuantity(@PathVariable("id") UUID chiTietGiayId, HttpSession httpSession, Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId).orElse(null);
        if (chiTietGiay != null) {
            response.put("success", true);
            response.put("quantity", chiTietGiay.getSoLuong());

            // Kiểm tra số lượng trong giỏ hàng session nếu chưa đăng nhập
            if (authentication == null || !authentication.isAuthenticated()) {
                Cart cartSession = (Cart) httpSession.getAttribute("cart");
                boolean errorOccurred = false;

                if (cartSession != null) {
                    long cartQuantity = cartSession.getItems().stream()
                            .filter(item -> item.getId().equals(chiTietGiayId))
                            .mapToLong(CartItem::getSoLuong)
                            .sum();

                    if (cartQuantity >= chiTietGiay.getSoLuong()) {
                        response.put("maxQuantityReached", true);
                        errorOccurred = true;
                    } else {
                        response.put("maxQuantityReached", false);
                    }
                } else {
                    response.put("maxQuantityReached", false);
                }

                if (errorOccurred) {
                    response.put("message", "Số lượng vượt quá giới hạn.");
                } else {
                    response.put("message", "Kiểm tra số lượng thành công.");
                }
            } else {
                // Kiểm tra số lượng trong giỏ hàng database nếu đã đăng nhập
                List<GioHangChiTiet> gioHangChiTiets = gioHangChiTietRepository.findByChiTietGiayId(chiTietGiayId);
                long databaseQuantity = gioHangChiTiets.stream().mapToLong(GioHangChiTiet::getSoLuong).sum();

                if (databaseQuantity >= chiTietGiay.getSoLuong()) {
                    response.put("maxQuantityReachedInDatabase", true);
                } else {
                    response.put("maxQuantityReachedInDatabase", false);
                }

                response.put("message", "Kiểm tra số lượng thành công.");
            }
        } else {
            response.put("success", false);
            response.put("message", "Sản phẩm không tồn tại.");
        }

        return ResponseEntity.ok(response);
    }








    @DeleteMapping("/removeAll")
    public ResponseEntity<String> xoaTatCaSanPhamTrongGioHang() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        this.soluongService.removeAllItems(taiKhoan);
        return ResponseEntity.ok("Đã xóa tất cả sản phẩm trong giỏ hàng.");
    }





}

