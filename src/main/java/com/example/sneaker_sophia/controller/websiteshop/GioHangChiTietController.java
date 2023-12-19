package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/giohangchitiet")
public class GioHangChiTietController {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private SoluongService soluongService;

    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;
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
    public ResponseEntity<Map<String, Object>> checkProductQuantity(
            @PathVariable("id") UUID chiTietGiayId,
            HttpSession httpSession,
            Authentication authentication) {
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
                    List<CartItem> items = cartSession.getItems();
                    if (items != null) {
                        long cartQuantity = items.stream()
                                .filter(item -> item.getId().equals(chiTietGiayId))
                                .mapToLong(CartItem::getSoLuong)
                                .sum();

                        if (cartQuantity >= chiTietGiay.getSoLuong()) {
                            response.put("maxQuantityReached", true);
                            response.put("cartQuantity", cartQuantity);
                            errorOccurred = true;
                        } else {
                            response.put("maxQuantityReached", false);
                            response.put("cartQuantity", cartQuantity);
                        }
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
                // Kiểm tra số lượng trong giỏ hàng của người dùng đăng nhập
                String username = authentication.getName();
                TaiKhoan taiKhoan = this.loginRepository.getTaiKhoanByEmail(username);
                if (taiKhoan != null) {
                    GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan); // Thay thế bằng phương thức tương ứng trong repository của bạn

                    if (gioHang != null) {
                        List<GioHangChiTiet> gioHangChiTiets = gioHang.getGioHangChiTiets();
                        long databaseQuantity = gioHangChiTiets.stream()
                                .filter(gioHangChiTiet -> gioHangChiTiet.getId().getChiTietGiay().getId().equals(chiTietGiayId))
                                .mapToLong(GioHangChiTiet::getSoLuong)
                                .sum();

                        if (databaseQuantity >= chiTietGiay.getSoLuong()) {
                            response.put("maxQuantityReachedInDatabase", true);
                        } else {
                            response.put("maxQuantityReachedInDatabase", false);
                            response.put("cartQuantity", databaseQuantity);
                        }

                        response.put("message", "Kiểm tra số lượng thành công.");
                    }
                }
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


    @GetMapping("/validate/{hoaDonId}")
    @ResponseBody
    public String validateProductQuantities(@PathVariable String hoaDonId) {
        Optional<HoaDon> optionalHoaDon = this.hoaDonWebRepository.findById(hoaDonId);

        if (optionalHoaDon.isPresent()) {
            HoaDon hoaDon = optionalHoaDon.get();

            if (hoaDon.getTrangThai() == 2) {
                for (HoaDonChiTiet chiTiet : hoaDon.getListHoaDonChiTiet()) {
                    ChiTietGiay chiTietGiay = chiTiet.getChiTietGiay();
                    int soLuongMua = chiTiet.getSoLuong();
                    int soLuongHienTai = chiTietGiay.getSoLuong();

                    if (soLuongHienTai < soLuongMua) {
                        return "Sản phẩm không đủ số lượng, vui lòng kiểm tra lại!";
                    }
                }

                return "Số lượng sản phẩm đủ, có thể thanh toán.";
            } else {
                return "Hóa đơn không hợp lệ.";
            }
        } else {
            return "Hóa đơn không tồn tại.";
        }
    }

    @GetMapping("/validate-cart")
    @ResponseBody
    public String validateCartQuantity(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        if (taiKhoan != null) {
            GioHang gioHang = this.cartService.getCartByTaiKhoan(taiKhoan);

            if (gioHang != null) {
                for (GioHangChiTiet chiTiet : gioHang.getGioHangChiTiets()) {
                    ChiTietGiay chiTietGiay = chiTiet.getId().getChiTietGiay();
                    int soLuongMua = chiTiet.getSoLuong();
                    int soLuongHienTai = chiTietGiay.getSoLuong();

                    if (soLuongHienTai < soLuongMua) {
                        return "Đơn hàng không đủ số lượng. Vui lòng kiểm tra giỏ hàng.";
                    }
                }

                return "success";
            } else {
                return "Giỏ hàng không tồn tại.";
            }
        } else {
            Cart cartSession = (Cart) session.getAttribute("cart");
            if (cartSession != null) {
                for (CartItem cart : cartSession.getItems()) {
                    Optional<ChiTietGiay> chiTietGiay = this.chiTietGiayRepository.findById(cart.getId());
                    int soLuongMua = cart.getSoLuong();
                    int soLuongHienTai = chiTietGiay.get().getSoLuong();
                    if (soLuongHienTai < soLuongMua) {
                        return "Đơn hàng không đủ số lượng. Vui lòng kiểm tra giỏ hàng.";
                    }
                }
                return "success";
            } else {
                return "Giỏ hàng không tồn tại.";
            }
        }
    }

    @GetMapping("/kiemtraemail")
    public Integer kiemTraEmail(@RequestParam(value = "email", required = false) String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(email);
        if (email != null && email.trim().length() > 0) {
            Boolean checktrung = loginRepository.existsByEmail(email);
            if (emailMatcher.matches()) {
                if (checktrung) {
                    return 0;
                }
            }else{
                return 3;
//                Sai ddijnh dajng
            }
        }

        return 1;
    }
}

