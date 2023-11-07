package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.DiaChiCheckoutService;
import com.example.sneaker_sophia.service.GioHangService;
import com.example.sneaker_sophia.service.ThanhToanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("check-out")
public class CheckoutController {
    @Autowired
    private ThanhToanService thanhToanService;
    @Autowired
    private DiaChiCheckoutService diaChiService;
    @Autowired
    private GioHangService gioHangService;

    @GetMapping("home")
    public String showCheckoutPage(Model model, Principal principal, HttpSession session) {
        double total = 0.0;
        DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());

        if (principal != null) {
            if (gioHang != null) {
                List<GioHangChiTiet> cartItems = gioHangService.getCartItems(gioHang.getId());
                if (cartItems == null || cartItems.isEmpty()) {
                    return "redirect:/cart/hien-thi";
                } else {
                    for (GioHangChiTiet item : cartItems) {
                        ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();

                        double subtotal = chiTietGiay.getGia() * item.getSoLuong();
                        total += subtotal;
                    }

                    if(diaChi != null){

                        session.setAttribute("tinh", diaChi.getTinh());
                        session.setAttribute("quan", diaChi.getQuanHuyen());
                        session.setAttribute("phuong", diaChi.getPhuongXa());
                    }else{
                        diaChi = new DiaChi();
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

    @PostMapping("/thanh-toan")
    public String thanhToan(Model model, @RequestParam("hinhThucThanhToan") Integer hinhThucThanhToan) {
        // Lấy thông tin tài khoản đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            // Lấy danh sách sản phẩm từ giỏ hàng của tài khoản đang đăng nhập
            List<GioHangChiTiet> cartItems = thanhToanService.getCartItemsByEmail(email);

            if (cartItems != null && !cartItems.isEmpty()) {
                // Gọi dịch vụ để thực hiện thanh toán
                thanhToanService.thucHienThanhToan(email, cartItems, hinhThucThanhToan);

                // Redirect hoặc hiển thị thông báo thanh toán thành công
                return "website/404"; // Hoặc trang kết quả thanh toán
            }
        }

        // Trả về trang giỏ hàng nếu không có sản phẩm trong giỏ
        return "redirect:/cart/hien-thi";
    }

}
