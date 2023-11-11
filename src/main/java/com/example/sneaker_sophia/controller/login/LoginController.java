package com.example.sneaker_sophia.controller.login;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.MyUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {
    @Autowired
    private CartService cartService;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping("home")
    public String home(Model model) {
//        model.addAttribute("data", taiKhoan);
        return "/login/dang_nhap";
    }

    @PostMapping("/login")
    public String loginSuccess(Model model, HttpSession httpSession) {
        // Người dùng đã đăng nhập
        Cart cartSession = (Cart) httpSession.getAttribute("cart");
        if (cartSession != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
                // Người dùng đã đăng nhập
                TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());
                cartService.mergeCarts(taiKhoan.getEmail(), cartSession);
                // Xóa giỏ hàng session sau khi đã thêm vào giỏ hàng của người dùng đăng nhập
                httpSession.removeAttribute("cart");
            }
        }
        return "website/productwebsite/shop-grid-sidebar-left";
    }



}
