package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("check-out")
public class CheckoutController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private ThanhToanService thanhToanService;
    @Autowired
    private DiaChiCheckoutService diaChiService;
    @Autowired
    private GioHangService gioHangService;

    @GetMapping("home")
    public String showCheckoutPage(Model model, HttpSession session) {
        double total = 0.0;
        DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());

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

                if (diaChi != null) {

                    session.setAttribute("tinh", diaChi.getTinh());
                    session.setAttribute("quan", diaChi.getQuanHuyen());
                    session.setAttribute("phuong", diaChi.getPhuongXa());
                } else {
                    diaChi = new DiaChi();
                }
                model.addAttribute("diaChi", diaChi);

                model.addAttribute("cartItems", cartItems);
                model.addAttribute("total", total);
                return "website/productwebsite/checkout";
            }
        }
        return "website/productwebsite/cart";
    }


    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        double total = 0.0;

        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            List<CartItem> cartItems = cart.getItems();

            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    // Kiểm tra chi tiết giày
                    if (item != null && item.getId() != null) {
                        double subtotal = item.getGia() * item.getSoLuong();
                        total += subtotal;
                    } else {
                        // Trường hợp không tìm thấy chi tiết giày, xử lý theo yêu cầu của bạn
                        return "redirect:/cart/hien-thi";
                    }
                }
                model.addAttribute("diaChi", new DiaChiDTO());

                // Thêm thông tin giỏ hàng vào Model để hiển thị trên trang thanh toán
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("total", total);

                return "website/productwebsite/checkoutSession";
            } else {
                // Giỏ hàng không có sản phẩm, chuyển hướng về trang giỏ hàng
                return "redirect:/cart/hien-thi";
            }
        } else {
            // Trường hợp không tìm thấy giỏ hàng trong session, chuyển hướng về trang giỏ hàng
            return "redirect:/cart/hien-thi";
        }
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


    @PostMapping("/thanhtoan")
    public String thanhToan(@ModelAttribute("diaChi") DiaChiDTO diaChi,
                            @RequestParam("hinhThucThanhToan") Integer hinhThucThanhToan,
                            Model model, HttpSession session) {
        try {
            Cart cart = (Cart) session.getAttribute("cart");
            List<CartItem> cartItems = cart.getItems();
            String matKhauNgauNhien = emailService.taoMatKhauNgauNhien();

            TaiKhoan taiKhoanMoi = emailService.taoTaiKhoanMoi(diaChi);
            emailService.guiEmailDangKyTaiKhoan(taiKhoanMoi.getEmail(), matKhauNgauNhien);

            diaChi.setTaiKhoan(taiKhoanMoi);

            emailService.themDiaChiVaoTaiKhoan(diaChi, taiKhoanMoi);

            HoaDon hoaDonMoi = emailService.taoHoaDonMoi(taiKhoanMoi, hinhThucThanhToan);
            emailService.themSanPhamVaoHoaDonChiTiet(cartItems, hoaDonMoi);

            emailService.guiEmailXacNhanThanhToan(taiKhoanMoi.getEmail(), hoaDonMoi);
            model.addAttribute("hoaDon", hoaDonMoi);
            session.removeAttribute("cart");
            System.out.println("Cart items: " + cartItems);


            return "redirect:/thanh-toan-thanh-cong";
        } catch (Exception e) {
            e.printStackTrace();

            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình thanh toán.");
            return "redirect:/thanh-toan-that-bai";
        }
    }



}
