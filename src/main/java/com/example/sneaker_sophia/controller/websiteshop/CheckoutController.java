package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;

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
        return "redirect:/cart/hien-thi";
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();

            List<GioHangChiTiet> cartItems = thanhToanService.getCartItemsByEmail(email);

            if (cartItems != null && !cartItems.isEmpty()) {
                thanhToanService.thucHienThanhToan(email, cartItems, hinhThucThanhToan);
                return "redirect:/check-out/success";
            }
        }
        return "redirect:/cart/hien-thi";
    }


    @PostMapping("/thanhtoan")
    public String thanhToan(@Valid @ModelAttribute("diaChi") DiaChiDTO diaChi, BindingResult result,
                            @RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            Model model, HttpSession session) {
        try {
            session.removeAttribute("tinh");
            session.removeAttribute("quan");
            session.removeAttribute("phuong");
            if (result.hasErrors()) {
                double total = 0.0;
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null) {
                    List<CartItem> cartItems = cart.getItems();
                    if (cartItems != null && !cartItems.isEmpty()) {
                        for (CartItem item : cartItems) {
                            if (item != null && item.getId() != null) {
                                double subtotal = item.getGia() * item.getSoLuong();
                                total += subtotal;
                            } else {
                                return "redirect:/cart/hien-thi";
                            }
                        }

                        session.setAttribute("tinh", diaChi.getTinh());
                        session.setAttribute("quan", diaChi.getQuanHuyen());
                        session.setAttribute("phuong", diaChi.getPhuongXa());
                        model.addAttribute("cartItems", cartItems);
                        model.addAttribute("total", total);

                        return "website/productwebsite/checkoutSession";
                    } else {
                        return "redirect:/cart/hien-thi";
                    }

                } else {
                    return "redirect:/cart/hien-thi";
                }
            }
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

            return "redirect:/check-out/success";

        } catch (Exception e) {
            e.printStackTrace();

            model.addAttribute("error", "Đã xảy ra lỗi trong quá trình thanh toán.");
            return "redirect:/shophia-store/home";
        }
    }


    @GetMapping("/paymentCallback")
    public String paymentCallback(
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
            @RequestParam(value = "vnp_Amount", required = false) String amountPaidString, HttpSession session) {
        String maHD = (String) session.getAttribute("maHD");
        if (responseCode != null && responseCode.equals("00")) {
            HoaDon hoaDon = hoaDonWebRepository.findByMaHoaDOn(maHD);
            System.out.println("Payment callback called!");
            if (hoaDon != null) {
                double amountPaid = getAmountPaidFromVnPayResponse(amountPaidString);
                hoaDon.setTongTien(hoaDon.getTongTien() + amountPaid);
                hoaDon.setTrangThai(3); // Assuming 2 represents a paid status, adjust it based on your needs
                hoaDonWebRepository.save(hoaDon);
                System.out.println("Payment callback called!");
                return "redirect/check-out/success";
            } else {
                return "redirect:/ádfadsf";
            }
        } else {
            return "redirect:/ádfadsf";
        }
    }

    private double getAmountPaidFromVnPayResponse(String amountPaidString) {
        if (amountPaidString != null) {
            try {
                return Double.parseDouble(amountPaidString) / 100000; // Assuming the amount is in VND, adjust if needed
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log the error or take appropriate action
            }
        }
        return 0.0;
    }


    @GetMapping("test")
    public String test() {
        return "website/testPhiShip";
    }

    @GetMapping("success")
    public String success() {
        return "website/productwebsite/success";
    }


}
