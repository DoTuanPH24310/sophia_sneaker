package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private GioHangRepository gioHangRepository;

    @GetMapping("hien-thi")
    public String viewCart(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());
        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        Long soLuong = this.cartService.countCartItems(authentication.getName());
        if(soLuong == 0){
            return "website/productwebsite/empty-cart";
        }
        model.addAttribute("soLuong", soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("cartItems", cartItems);
        return "website/productwebsite/cart";
    }

    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") UUID chiTietGiayId, Model model) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());

            cartService.addToCart(taiKhoan.getEmail(), chiTietGiayId);

            GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);
            List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());

            double totalCartPrice = cartItems.stream()
                    .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                    .sum();
            Long soLuong = this.cartService.countCartItems(authentication.getName());
            model.addAttribute("soLuong", soLuong);
            model.addAttribute("totalCartPrice", totalCartPrice);
            model.addAttribute("gioHang", gioHang);
            return "redirect:/cart/hien-thi";
        } catch (UsernameNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "website/productwebsite/error";
        }
    }

    @GetMapping("/removeProductCart")
    public String removeFromCart(@RequestParam("gioHangId") UUID gioHangId,
                                 @RequestParam("chiTietGiayId") UUID chiTietGiayId) {
        try {
            this.cartService.removeFromCart(gioHangId, chiTietGiayId);
            return "redirect:/cart/hien-thi"; // Chuyển hướng về trang giỏ hàng hoặc trang khác
        } catch (Exception e) {
            return "website/productwebsite/cart"; // Xử lý lỗi và chuyển hướng với thông báo lỗi
        }
    }




}
