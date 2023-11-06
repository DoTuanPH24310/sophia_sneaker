package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.DiaChiCheckoutService;
import com.example.sneaker_sophia.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("check-out")
public class CheckoutController {

    @Autowired
    private DiaChiCheckoutService diaChiService;
    @Autowired
    private GioHangService gioHangService;

    @GetMapping("home")
    public String showCheckoutPage(Model model, Principal principal) {
        double total = 0.0;
        DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());

        if (principal != null) {
            if (gioHang == null) {
                List<GioHangChiTiet> cartItems = gioHangService.getCartItems(gioHang.getId());
                if (cartItems == null || cartItems.isEmpty()) {
                    return "redirect:/cart/hien-thi";
                } else {
                    for (GioHangChiTiet item : cartItems) {
                        ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();

                        double subtotal = chiTietGiay.getGia() * item.getSoLuong();
                        total += subtotal;
                    }
                    model.addAttribute("diaChi", diaChi);

                    model.addAttribute("cartItems", cartItems);
                    model.addAttribute("total", total);
                    return "website/productwebsite/checkout";
                }
            }
        }
        return "website/productwebsite/cart";
    }



}
