package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.DiaChiLoGin;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.HinhThucThanhToanWebRepository;
import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
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
    private LoginRepository loginRepository;
    @Autowired
    private HinhThucThanhToanWebRepository hinhThucThanhToanWebRepository;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
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
        double tongTienGiam = 0.0;
        int soLuongPhieuGiamDaSuDung = 0;
        int soLuongGiam = 0;
        int tongSoLuongGiam = 0;

        DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        model.addAttribute("email", taiKhoan.getEmail());
        session.setAttribute("tinh", "-1");
        session.setAttribute("quan", "-1");
        session.setAttribute("phuong", "-1");
        if (gioHang != null) {

            List<GioHangChiTiet> cartItems = gioHangService.getCartItems(gioHang.getId());
            if (authentication != null) {
                if (cartItems == null || cartItems.isEmpty()) {
                    return "redirect:/cart/hien-thi";
                } else {
                    for (GioHangChiTiet item : cartItems) {
                        ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();
                        int soLuongMua = item.getSoLuong();
                        List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();
                        total += item.getId().getChiTietGiay().getGia() * item.getSoLuong();
                        double tongTienDonHang = total;

                        for (CTG_KhuyenMai ctg : listCTG_KM) {
                            // Kiểm tra trạng thái giảm giá
                            if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                                if (ctg.getId().getVoucher() != null) {
                                    soLuongGiam = ctg.getId().getVoucher().getSoLuong();
                                }
                                int soLuongGiamApDung = Math.min(soLuongGiam - soLuongPhieuGiamDaSuDung, soLuongMua);
                                tongSoLuongGiam = soLuongGiamApDung;

                                double donGia = chiTietGiay.getGia();
                                int giam = ctg.getId().getVoucher().getPhanTramGiam();
                                double tienGiam = donGia * giam / 100 * soLuongGiamApDung;
                                tongTienGiam += tienGiam;
                                if (!listCTG_KM.isEmpty()) {
                                    tongTienDonHang = total - tongTienGiam;
                                }
                            }
                        }
                        model.addAttribute("total", tongTienDonHang);

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
                    model.addAttribute("tongSoLuongGiam", tongSoLuongGiam);
                    return "website/productwebsite/checkout";
                }
            }
            return "redirect:/cart/hien-thi";
        }
        return "redirect:/cart/hien-thi";
    }


    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        double total = 0.0;
        double tongTienGiam = 0.0;
        int soLuongPhieuGiamDaSuDung = 0;
        int soLuongGiam = 0;
        int tongSoLuongGiam = 0;
        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            List<CartItem> cartItems = cart.getItems();

            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(item.getId()).orElse(null);
                    int soLuongMua = item.getSoLuong();
                    List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();
                    total += chiTietGiay.getGia() * item.getSoLuong();
                    double tongTienDonHang = total;

                    for (CTG_KhuyenMai ctg : listCTG_KM) {
                        // Kiểm tra trạng thái giảm giá
                        if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                            if (ctg.getId().getVoucher() != null) {
                                soLuongGiam = ctg.getId().getVoucher().getSoLuong();
                            }
                            int soLuongGiamApDung = Math.min(soLuongGiam - soLuongPhieuGiamDaSuDung, soLuongMua);
                            tongSoLuongGiam = soLuongGiamApDung;

                            double donGia = chiTietGiay.getGia();
                            int giam = ctg.getId().getVoucher().getPhanTramGiam();
                            double tienGiam = donGia * giam / 100 * soLuongGiamApDung;
                            tongTienGiam += tienGiam;
                            if (!listCTG_KM.isEmpty()) {
                                tongTienDonHang = total - tongTienGiam;
                            }
                        }
                    }
                    model.addAttribute("total", tongTienDonHang);

                }
                model.addAttribute("diaChi", new DiaChiDTO());
                session.setAttribute("tinh", "-1");
                session.setAttribute("quan", "-1");
                session.setAttribute("phuong", "-1");
                // Thêm thông tin giỏ hàng vào Model để hiển thị trên trang thanh toán
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("tongTienGiam", tongTienGiam);
                model.addAttribute("tongSoLuongGiam", tongSoLuongGiam);

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
    public String thanhToan(Model model, @Valid @ModelAttribute("diaChi") DiaChiLoGin diaChiDTO, BindingResult result,
                            @RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            @RequestParam(value = "diaChiCuThe", required = false) String diaChiCuThe,
                            @RequestParam(value = "thanhPho", required = false) String tinh,
                            @RequestParam(value = "huyen", required = false) String huyen,
                            @RequestParam(value = "xa", required = false) String xa,
                            HttpSession session) {
        double total = 0.0;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
            List<GioHangChiTiet> cartItems = thanhToanService.getCartItemsByEmail(email);
            for (GioHangChiTiet item : cartItems) {
                ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();

                double giaBan = chiTietGiay.getGia();
                int soLuong = item.getSoLuong();

                if (soLuong > 0) {
                    double subtotal = giaBan * soLuong;
                    total += subtotal;
                } else {
                    double giaKhuyenMai = (Double) session.getAttribute("giaMoi_" + chiTietGiay.getId());
                    total += giaKhuyenMai;
                }
            }
            if (result.hasErrors()) {
                DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();

                GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());
                model.addAttribute("email", taiKhoan.getEmail());
                if (gioHang != null) {
                    if (cartItems == null || cartItems.isEmpty()) {
                        return "redirect:/cart/hien-thi";
                    } else {

                        session.setAttribute("tinh", diaChi.getTinh());
                        session.setAttribute("quan", diaChi.getQuanHuyen());
                        session.setAttribute("phuong", diaChi.getPhuongXa());
                        model.addAttribute("cartItems", cartItems);
                        model.addAttribute("total", total);
                        return "website/productwebsite/checkout";
                    }
                }
            }
            if (cartItems != null && !cartItems.isEmpty()) {
                this.thanhToanService.capNhatDiaChi(diaChiDTO, taiKhoan);
                thanhToanService.thucHienThanhToan(email, cartItems, hinhThucThanhToan, diaChiCuThe, tinh, huyen, xa);
                return "redirect:/check-out/success";
            }
        }
        return "redirect:/cart/hien-thi";
    }

    @PostMapping("/thanhtoan")
    public String thanhToan(@Valid @ModelAttribute("diaChi") DiaChiDTO diaChi, BindingResult result,
                            @RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            @RequestParam(value = "diaChiCuThe", required = false) String diaChiCuThe,
                            @RequestParam(value = "thanhPho", required = false) String tinh,
                            @RequestParam(value = "huyen", required = false) String huyen,
                            @RequestParam(value = "xa", required = false) String xa,
                            Model model, HttpSession session) {
        try {
            session.removeAttribute("tinh");
            session.removeAttribute("quan");
            session.removeAttribute("phuong");
            if (result.hasErrors()) {
                double total = 0.0;
                Cart cart = (Cart) session.getAttribute("cart");
                boolean tonTai = this.loginRepository.existsByEmail(diaChi.getEmail());
                if (tonTai) {
                    session.setAttribute("tinh", "-1");
                    session.setAttribute("quan", "-1");
                    session.setAttribute("phuong", "-1");
                    result.rejectValue("email", "error.email", "Email đã tồn tại trong hệ thống");

                    return "website/productwebsite/checkoutSession";
                }
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

            HoaDon hoaDonMoi = emailService.taoHoaDonMoi(taiKhoanMoi, hinhThucThanhToan, diaChiCuThe, tinh, huyen, xa);
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
//            HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
            System.out.println("Payment callback called!");
            if (hoaDon != null) {
                double amountPaid = getAmountPaidFromVnPayResponse(amountPaidString);
                HinhThucThanhToan hinhThuc = hinhThucThanhToanWebRepository.findByHoaDon(hoaDon);
                if (hinhThuc != null) {
                    hinhThuc.setSoTien(hinhThuc.getSoTien() + amountPaid);
                    this.hinhThucThanhToanWebRepository.save(hinhThuc); // Update HinhThucThanhToan in the database
                    hoaDon.setTrangThai(3);

                }
                return "redirect:/check-out/success";
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
                return Double.parseDouble(amountPaidString) / 100; // Assuming the amount is in VND, adjust if needed
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
