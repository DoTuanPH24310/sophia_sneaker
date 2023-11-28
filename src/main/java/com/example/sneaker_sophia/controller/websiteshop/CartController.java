package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.KhuyenMaiWebService;
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
    private KhuyenMaiWebService khuyenMaiWebService;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private CartService cartService;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private GioHangRepository gioHangRepository;

    @GetMapping("hien-thi")
    public String viewCart(Model model, HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());

            if (cartItems.isEmpty()) {
                return "website/productwebsite/empty-cart";
            }

            for (GioHangChiTiet cartItem : cartItems) {
                ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();
                khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
            }

            double totalCartPrice = cartItems.stream()
                    .mapToDouble(item -> {
                        ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();
                        khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
                        return (Double) httpSession.getAttribute("giaMoi_" + chiTietGiay.getId()) * item.getSoLuong();
                    })
                    .sum();

            Long soLuong = this.cartService.countCartItems(authentication.getName());

            model.addAttribute("soLuong", soLuong);
            model.addAttribute("totalCartPrice", totalCartPrice);
            model.addAttribute("cartItems", cartItems);
            model.addAttribute("isLoggedIn", true);
            return "website/productwebsite/cart";
        } else {
            Cart cart = (Cart) httpSession.getAttribute("cart");

            if (cart == null || (cart != null && cart.getItems().isEmpty())) {
                return "website/productwebsite/empty-cart";
            } else if (cart != null) {
                for (CartItem cartItem : cart.getItems()) {
                    UUID chiTietGiayId = cartItem.getId();
                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(chiTietGiayId).orElse(null);
                    if (chiTietGiay != null) {
                        khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
                    }
                }

                List<CartItem> cartItems = cart.getItems();
                double totalCartPrice = cartItems.stream()
                        .mapToDouble(item -> item.getGia() * item.getSoLuong())
                        .sum();
                Long soLuong = cartItems.stream().mapToLong(CartItem::getSoLuong).sum();
                model.addAttribute("soLuong", soLuong);
                model.addAttribute("totalCartPrice", totalCartPrice);
                model.addAttribute("cartItem", cartItems);
            }
            return "website/productwebsite/cartSession";
        }
    }


    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") UUID chiTietGiayId, Model model, HttpSession httpSession) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
                // Người dùng đã đăng nhập
                TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());
                cartService.addToCart(taiKhoan.getEmail(), chiTietGiayId);

                // Hiển thị giỏ hàng từ database
                GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);
                List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());

                double totalCartPrice = cartItems.stream()
                        .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                        .sum();
                Long soLuong = this.cartService.countCartItems(authentication.getName());
                model.addAttribute("soLuong", soLuong);
                model.addAttribute("totalCartPrice", totalCartPrice);
                model.addAttribute("gioHang", gioHang);
            } else {
                // Người dùng chưa đăng nhập
                cartService.addToCartNoLogin(chiTietGiayId, httpSession);

                // Hiển thị giỏ hàng từ session
                Cart cart = (Cart) httpSession.getAttribute("cart");
                if (cart != null) {
                    List<CartItem> cartItems = cart.getItems();
                    double totalCartPrice = cartItems.stream()
                            .mapToDouble(item -> item.getGia() * item.getSoLuong())
                            .sum();
                    Long soLuong = cartItems.stream().mapToLong(CartItem::getSoLuong).sum();
                    model.addAttribute("soLuong", soLuong);
                    model.addAttribute("totalCartPrice", totalCartPrice);
                    model.addAttribute("cartItems", cartItems);
                }
            }

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
