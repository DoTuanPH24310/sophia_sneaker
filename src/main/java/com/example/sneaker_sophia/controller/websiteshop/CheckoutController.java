package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.DiaChiLoGin;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.*;
import com.example.sneaker_sophia.validate.AlertInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;
    @Autowired
    private DiaChiCheckoutService diaChiService;
    @Autowired
    private GioHangService gioHangService;
    @Resource(name = "diaChiService")
    DiaChiService diaChiServiceTQ;
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;

    private static final Object lock = new Object();

    @GetMapping("home")
    public String showCheckoutPage(Model model, HttpSession session) {
        double total = 0.0;
        double tongTienGiam = 0.0;
        int soLuongPhieuGiamDaSuDung = 0;
        int soLuongGiam = 0;
        int tongSoLuongGiam = 0;
        boolean isValidCheckout = true; // Thêm biến kiểm tra

        DiaChi diaChi = accountService.getDiaChiMacDinhCuaTaiKhoanDangNhap();
        DiaChiLoGin diaChiDTO;
        if (diaChi != null) {
            diaChiDTO = new DiaChiLoGin();
            BeanUtils.copyProperties(diaChi, diaChiDTO);
        } else {
            diaChiDTO = new DiaChiLoGin();
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        model.addAttribute("email", taiKhoan.getEmail());
        double totalCartItemsPrice = 0.0;
        session.setAttribute("tinh", "-1");
        session.setAttribute("quan", "-1");
        session.setAttribute("phuong", "-1");
        if (gioHang != null) {

            List<GioHangChiTiet> cartItems = gioHangService.getCartItems(gioHang.getId());
            if (authentication != null) {
                if (cartItems == null || cartItems.isEmpty()) {
                    return "redirect:/cart/hien-thi";
                } else {
                    Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                    Map<UUID, Integer> voucherUsageCount = new HashMap<>();

                    for (GioHangChiTiet cartItem : cartItems) {
                        double totalCartItemPrice = calculateTotalCartItemPrice(cartItem, voucherUsageCount);
                        cartItemTotalPrices.put(cartItem.getId().getChiTietGiay().getId(), totalCartItemPrice);
                        totalCartItemsPrice += totalCartItemPrice;

                    }
                    model.addAttribute("discountedProductPrices", cartItemTotalPrices);

                    for (GioHangChiTiet item : cartItems) {
                        ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();
                        int soLuongMua = item.getSoLuong();
                        int soLuongCoSan = chiTietGiay.getSoLuong(); // Số lượng có sẵn của sản phẩm

                        if (soLuongMua > soLuongCoSan) {
                            alertInfo.alert("errOnline", "Không thể tiếp tục khi giỏ hàng có sản phẩm đã hết!");
                            isValidCheckout = false;
                            break;
                        }

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
                        model.addAttribute("total", totalCartItemsPrice);

                    }

                    if (!isValidCheckout) {
                        // Nếu có ít nhất một sản phẩm không phù hợp, chuyển hướng đến trang giỏ hàng
                        return "redirect:/cart/hien-thi";
                    }

                    if (diaChi != null) {
                        session.setAttribute("tinh", diaChi.getTinh());
                        session.setAttribute("quan", diaChi.getQuanHuyen());
                        session.setAttribute("phuong", diaChi.getPhuongXa());
                    } else {
                        diaChi = new DiaChi();
                    }
                    session.setAttribute("selectedProvince", diaChi.getTinh());
                    model.addAttribute("diaChi", diaChiDTO);
                    model.addAttribute("cartItems", cartItems);
                    model.addAttribute("tongSoLuongGiam", tongSoLuongGiam);
                    model.addAttribute("listDC", diaChiServiceTQ.getAllDCByIdkh(taiKhoan.getId()));
                    session.setAttribute("idkhOL", taiKhoan.getId());
                    model.addAttribute("isValidCheckout", isValidCheckout); // Thêm biến vào model
                    return "website/productwebsite/checkout";
                }
            }
            return "redirect:/cart/hien-thi";
        }
        return "redirect:/cart/hien-thi";
    }

    private double calculateTotalCartItemPrice(GioHangChiTiet cartItem, Map<UUID, Integer> voucherUsageCount) {
        ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();
        int soLuongMua = cartItem.getSoLuong();
        double total = chiTietGiay.getGia() * soLuongMua;

        List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();

        for (CTG_KhuyenMai ctg : listCTG_KM) {
            if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                UUID voucherId = ctg.getId().getVoucher().getId();

                // Kiểm tra xem đã sử dụng hết giảm giá từ voucher chưa
                int soLuongGiamConLai = ctg.getId().getVoucher().getSoLuong() - voucherUsageCount.getOrDefault(voucherId, 0);
                int soLuongGiamApDung = Math.min(soLuongGiamConLai, soLuongMua);

                if (soLuongGiamApDung > 0) {
                    double donGia = chiTietGiay.getGia();
                    int phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                    double tienGiam = donGia * phanTramGiam / 100 * soLuongGiamApDung;

                    // Áp dụng giảm giá cho sản phẩm
                    total -= tienGiam;

                    // Tăng số lượng giảm giá đã sử dụng cho voucher
                    voucherUsageCount.put(voucherId, voucherUsageCount.getOrDefault(voucherId, 0) + soLuongGiamApDung);
                }
            }
        }

        return total;
    }

    @GetMapping("/checkout")
    public String showCheckout(Model model, HttpSession session) {
        double total = 0.0;
        double tongTienGiam = 0.0;
        int soLuongPhieuGiamDaSuDung = 0;
        int soLuongGiam = 0;
        int tongSoLuongGiam = 0;
        boolean isValidCheckout = true; // Thêm biến kiểm tra
        double totalCartItemsPrice = 0.0;

        // Lấy giỏ hàng từ session
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            List<CartItem> cartItems = cart.getItems();
            Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
            Map<UUID, Integer> voucherUsageCount = new HashMap<>();
            for (CartItem item : cartItems) {
                UUID chiTietGiayId = item.getId();
                double totalCartItemPrice = calculateTotalCartItemPricec(item, voucherUsageCount);
                cartItemTotalPrices.put(chiTietGiayId, totalCartItemPrice);
                totalCartItemsPrice += totalCartItemPrice;
            }
            model.addAttribute("discountedProductPrices", cartItemTotalPrices);
            if (cartItems != null && !cartItems.isEmpty()) {
                for (CartItem item : cartItems) {
                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(item.getId()).orElse(null);
                    int soLuongMua = item.getSoLuong();
                    int soLuongCoSan = chiTietGiay.getSoLuong();

                    if (soLuongMua > soLuongCoSan) {
                        alertInfo.alert("errOnline", "Không thể tiếp tục khi giỏ hàng có sản phẩm đã hết!");
                        isValidCheckout = false;
                        break;
                    }

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
                    model.addAttribute("total", totalCartItemsPrice);
                }

                if (!isValidCheckout) {
                    return "redirect:/cart/hien-thi";
                }

                model.addAttribute("diaChi", new DiaChiDTO());
                session.setAttribute("tinh", "-1");
                session.setAttribute("quan", "-1");
                session.setAttribute("phuong", "-1");

                model.addAttribute("cartItems", cartItems);
                model.addAttribute("tongTienGiam", tongTienGiam);
                model.addAttribute("tongSoLuongGiam", tongSoLuongGiam);
                model.addAttribute("isValidCheckout", isValidCheckout); // Thêm biến vào model

                return "website/productwebsite/checkoutSession";
            } else {
                return "redirect:/cart/hien-thi";
            }
        } else {
            return "redirect:/cart/hien-thi";
        }
    }

    private double calculateTotalCartItemPricec(CartItem cartItem, Map<UUID, Integer> voucherUsageCount) {
        UUID chiTietGiayId = cartItem.getId();
        ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(chiTietGiayId).orElse(null);
        int soLuongMua = cartItem.getSoLuong();
        double total = chiTietGiay.getGia() * soLuongMua;

        List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();

        for (CTG_KhuyenMai ctg : listCTG_KM) {
            if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                UUID voucherId = ctg.getId().getVoucher().getId();

                // Kiểm tra xem đã sử dụng hết giảm giá từ voucher chưa
                int soLuongGiamConLai = ctg.getId().getVoucher().getSoLuong() - voucherUsageCount.getOrDefault(voucherId, 0);
                int soLuongGiamApDung = Math.min(soLuongGiamConLai, soLuongMua);

                if (soLuongGiamApDung > 0) {
                    double donGia = chiTietGiay.getGia();
                    int phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                    double tienGiam = donGia * phanTramGiam / 100 * soLuongGiamApDung;

                    // Áp dụng giảm giá cho sản phẩm
                    total -= tienGiam;

                    // Tăng số lượng giảm giá đã sử dụng cho voucher
                    voucherUsageCount.put(voucherId, voucherUsageCount.getOrDefault(voucherId, 0) + soLuongGiamApDung);
                }
            }
        }

        return total;
    }

    @PostMapping("/thanh-toan")
    public String thanhToan(Model model, @Valid @ModelAttribute("diaChi") DiaChiLoGin diaChiDTO, BindingResult result,
                            @RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            @RequestParam(value = "diaChiCuThe", required = false) String diaChiCuThe,
                            @RequestParam(value = "thanhPho", required = false) String tinh,
                            @RequestParam(value = "huyen", required = false) String huyen,
                            @RequestParam(value = "xa", required = false) String xa,
                            @RequestParam(value = "ghiChu", required = false) String ghiChu,
                            Double phiVanChuyen,
                            HttpSession session) {
        synchronized (lock) {

            double total = 0.0;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());
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
                    DiaChi diaChi = accountService.getDiaChiMacDinhCuaTaiKhoanDangNhap();
                    Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                    Map<UUID, Integer> voucherUsageCount = new HashMap<>();

                    for (GioHangChiTiet cartItem : cartItems) {
                        double totalCartItemPrice = calculateTotalCartItemPrice(cartItem, voucherUsageCount);
                        cartItemTotalPrices.put(cartItem.getId().getChiTietGiay().getId(), totalCartItemPrice);
                    }
                    model.addAttribute("discountedProductPrices", cartItemTotalPrices);
                    model.addAttribute("email", taiKhoan.getEmail());
                    if (gioHang != null) {
                        if (cartItems == null || cartItems.isEmpty()) {
                            return "redirect:/cart/hien-thi";
                        } else {
                            session.setAttribute("selectedProvince", diaChiDTO.getTinh());
                            session.setAttribute("tinh", diaChiDTO.getTinh());
                            session.setAttribute("quan", diaChiDTO.getQuanHuyen());
                            session.setAttribute("phuong", diaChiDTO.getPhuongXa());
                            model.addAttribute("cartItems", cartItems);
                            model.addAttribute("total", total);
                            return "website/productwebsite/checkout";
                        }
                    }
                }
                if (total < 2000000) {
                    if (diaChiDTO.getTinh() == 1) {
                        phiVanChuyen = 20000.0;
                    } else {
                        phiVanChuyen = 30000.0;
                    }
                } else {
                    phiVanChuyen = 0.0;
                }
                for (GioHangChiTiet item : cartItems) {
                    ChiTietGiay chiTietGiay = item.getId().getChiTietGiay();
                    int soLuongHienTai = chiTietGiay.getSoLuong();

                    if (soLuongHienTai < 1) {
                        alertInfo.alert("errOnline", "Sản phẩm đã hết hàng.");
                        return "redirect:/cart/hien-thi";
                    }

                    // Kiểm tra số lượng sản phẩm còn đủ cho người đăng nhập và người không đăng nhập hay không
                    if (soLuongHienTai < item.getSoLuong()) {
                        alertInfo.alert("errOnline", "Số lượng sản phẩm không đủ.");
                        return "redirect:/cart/hien-thi";
                    }
                }
                if (cartItems != null && !cartItems.isEmpty()) {
                    if (hinhThucThanhToan == null) {
                        alertInfo.alert("errOnline", "Chưa chọn hình thức thanh toán!");
                    } else {
//                        this.thanhToanService.capNhatDiaChi(diaChiDTO, taiKhoan);
                        thanhToanService.thucHienThanhToan(email, diaChiDTO, cartItems, hinhThucThanhToan, diaChiCuThe, tinh, huyen, xa, phiVanChuyen, ghiChu);
                        return "redirect:/check-out/success";
                    }
                }

            }
            return "redirect:/cart/hien-thi";
        }
    }

    @PostMapping("/thanhtoan")
    public String thanhToan(@Valid @ModelAttribute("diaChi") DiaChiDTO diaChi, BindingResult result,
                            @RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            @RequestParam(value = "diaChiCuThe", required = false) String diaChiCuThe,
                            @RequestParam(value = "thanhPho", required = false) String tinh,
                            @RequestParam(value = "huyen", required = false) String huyen,
                            @RequestParam(value = "xa", required = false) String xa,
                            @RequestParam(value = "ghiChu", required = false) String ghiChu,
                            @RequestParam(value = "ten", required = false) String ten,
                            @RequestParam(value = "sdt", required = false) String sdt,
                            Double phiVanChuyen,
                            Model model, HttpSession session) {
        synchronized (lock) {

            try {
                double total = 0.0;
                Cart cart = (Cart) session.getAttribute("cart");
                List<CartItem> cartItems = cart.getItems();
                for (CartItem item : cartItems) {
                    if (item == null || item.getId() == null || item.getSoLuong() <= 0) {
                        alertInfo.alert("errOnline", "Sản phẩm đã hết hàng hoặc không hợp lệ.");
                        return "redirect:/cart/hien-thi";
                    }

                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(item.getId()).orElse(null);
                    int soLuongHienTai = chiTietGiay.getSoLuong();
                    if (soLuongHienTai < item.getSoLuong()) {
                        alertInfo.alert("errOnline", "Số lượng sản phẩm " + chiTietGiay.getTen() + " không đủ.");
                        return "redirect:/cart/hien-thi";
                    }

                    total += item.getGia() * item.getSoLuong();
                }
                double totalCartItemsPrice = 0.0;
                session.removeAttribute("tinh");
                session.removeAttribute("quan");
                session.removeAttribute("phuong");
                if (diaChi.getEmail() != null) {
                    if (diaChi.getEmail().trim().length() != 0) {
                        boolean tonTai = this.loginRepository.existsByEmail(diaChi.getEmail());
                        if (tonTai) {
                            Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                            Map<UUID, Integer> voucherUsageCount = new HashMap<>();
                            for (CartItem item : cartItems) {
                                UUID chiTietGiayId = item.getId();
                                double totalCartItemPrice = calculateTotalCartItemPricec(item, voucherUsageCount);
                                cartItemTotalPrices.put(chiTietGiayId, totalCartItemPrice);
                                totalCartItemsPrice += totalCartItemPrice;
                            }
                            model.addAttribute("discountedProductPrices", cartItemTotalPrices);
                            if (cart != null) {
                                if (cartItems != null && !cartItems.isEmpty()) {

                                    session.setAttribute("selectedProvince", diaChi.getTinh());
                                    session.setAttribute("tinh", diaChi.getTinh());
                                    session.setAttribute("quan", diaChi.getQuanHuyen());
                                    session.setAttribute("phuong", diaChi.getPhuongXa());
                                    result.rejectValue("email", "error.email", "Email đã tồn tại trong hệ thống.");
                                    model.addAttribute("cartItems", cartItems);
                                    model.addAttribute("total", totalCartItemsPrice);

                                    return "website/productwebsite/checkoutSession";
                                } else {
                                    return "redirect:/cart/hien-thi";
                                }

                            } else {
                                return "redirect:/cart/hien-thi";
                            }
                        }
                    }
                }
                if (result.hasErrors()) {
                    Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                    Map<UUID, Integer> voucherUsageCount = new HashMap<>();
                    for (CartItem item : cartItems) {
                        UUID chiTietGiayId = item.getId();
                        double totalCartItemPrice = calculateTotalCartItemPricec(item, voucherUsageCount);
                        cartItemTotalPrices.put(chiTietGiayId, totalCartItemPrice);
                    }
                    model.addAttribute("discountedProductPrices", cartItemTotalPrices);
                    if (cart != null) {
                        if (cartItems != null && !cartItems.isEmpty()) {
                            session.setAttribute("selectedProvince", diaChi.getTinh());
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


                for (CartItem item : cartItems) {
                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(item.getId()).orElse(null);
                    int soLuongHienTai = chiTietGiay.getSoLuong();

                    if (soLuongHienTai < 1) {
                        alertInfo.alert("errOnline", "Sản phẩm đã hết hàng.");
                        return "redirect:/cart/hien-thi";
                    }

                    // Kiểm tra số lượng sản phẩm còn đủ cho người đăng nhập và người không đăng nhập hay không
                    if (soLuongHienTai < item.getSoLuong()) {
                        alertInfo.alert("errOnline", "Số lượng sản phẩm không đủ.");
                        return "redirect:/cart/hien-thi";
                    }
                }
                if (total > 2000000) {
                    phiVanChuyen = 0.0;
                } else {
                    if (diaChi.getTinh() == 1) {
                        phiVanChuyen = 20000.0;
                    } else {
                        phiVanChuyen = 30000.0;
                    }
                }
                if (total >= 1000000000) {
                    if (cart != null) {
                        if (cartItems != null && !cartItems.isEmpty()) {
                            for (CartItem item : cartItems) {
                                if (item != null && item.getId() != null) {
                                    double subtotal = item.getGia() * item.getSoLuong();
                                    total += subtotal;
                                } else {
                                    return "redirect:/cart/hien-thi";
                                }
                            }
                            session.setAttribute("selectedProvince", diaChi.getTinh()); // Thêm dòng này
                            alertInfo.alert("errOnline", "Số tiền quá lớn không thể thực hiện thanh toán.");

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
                model.addAttribute("phiVanChuyen", phiVanChuyen);

                if (diaChi == null || StringUtils.isEmpty(diaChi.getEmail())) {
                    HoaDon hoaDonMoi = emailService.taoHoaDonMoi(null, hinhThucThanhToan, diaChiCuThe, tinh, huyen, xa, phiVanChuyen, ghiChu, ten, sdt);
                    emailService.themSanPhamVaoHoaDonChiTiet(cartItems, hoaDonMoi, phiVanChuyen);
                }
                if (diaChi != null && !StringUtils.isEmpty(diaChi.getEmail())) {

                    TaiKhoan taiKhoanMoi = emailService.taoTaiKhoanMoi(diaChi);
                    if (taiKhoanMoi != null) {
                        HoaDon hoaDonMoi = emailService.taoHoaDonMoi(taiKhoanMoi, hinhThucThanhToan, diaChiCuThe, tinh, huyen, xa, phiVanChuyen, ghiChu, diaChi.getTen(), diaChi.getSdt());
                        emailService.themSanPhamVaoHoaDonChiTiet(cartItems, hoaDonMoi, phiVanChuyen);
                        diaChi.setTaiKhoan(taiKhoanMoi);
                        emailService.themDiaChiVaoTaiKhoan(diaChi, taiKhoanMoi);
                        if (hinhThucThanhToan == 3) {
                            emailService.guiEmailXacNhanThanhToan(taiKhoanMoi.getEmail(), hoaDonMoi);
                        }
                        session.setAttribute("mailTaiKhoan", taiKhoanMoi.getEmail());
                    } else {
                        model.addAttribute("error", "Failed to create a new account.");
                        return "redirect:/shophia-store/home";
                    }
                }

                session.removeAttribute("cart");
                return "redirect:/check-out/success";

            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "Đã xảy ra lỗi trong quá trình thanh toán.");
                return "redirect:/shophia-store/home";
            }
        }
    }


    @GetMapping("/paymentCallback")
    public String paymentCallback(
            @RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) String transactionNo,
            @RequestParam(value = "vnp_Amount", required = false) String amountPaidString, HttpSession session) {
        String maHD = (String) session.getAttribute("maHD");
        String mailTaiKhoan = (String) session.getAttribute("mailTaiKhoan");
        if (responseCode != null && responseCode.equals("00")) {
            HoaDon hoaDon = hoaDonWebRepository.findByMaHoaDOn(maHD);
//            HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
            System.out.println("Payment callback called!");
            if (hoaDon != null) {
                double amountPaid = getAmountPaidFromVnPayResponse(amountPaidString);
                HinhThucThanhToan hinhThuc = hinhThucThanhToanWebRepository.findByHoaDon(hoaDon);
                LichSuHoaDon lichSuHoaDon = this.lichSuHoaDonWebRepository.findByHoaDon(hoaDon);
                if (lichSuHoaDon != null) {
                    lichSuHoaDon.setPhuongThuc("3");
                    this.lichSuHoaDonWebRepository.save(lichSuHoaDon);
                }
                if (hinhThuc != null) {
                    hinhThuc.setSoTien(hinhThuc.getSoTien() + amountPaid);
                    this.hinhThucThanhToanWebRepository.save(hinhThuc);
                    if (hoaDon.getTrangThai() == 2) {
                        for (HoaDonChiTiet chiTiet : hoaDon.getListHoaDonChiTiet()) {
                            ChiTietGiay chiTietGiay = chiTiet.getChiTietGiay();
                            int soLuongMua = chiTiet.getSoLuong();
                            int soLuongHienTai = chiTietGiay.getSoLuong();
                            if (soLuongHienTai >= soLuongMua) {
                                chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                                chiTietGiayRepository.save(chiTietGiay);
                            } else {
                                return "redirect:/sophia-store/home";
                            }
                        }
                    }
                    hoaDon.setTrangThai(3);
                    this.hoaDonWebRepository.save(hoaDon);
                    emailService.guiEmailXacNhanThanhToan(mailTaiKhoan, hoaDon);
                }
                return "redirect:/check-out/success";
            } else {
                return "redirect:/sophia-store/home";
            }
        } else {
            return "redirect:/sophia-store/home";
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


    @PostMapping("adddc")
    public String adddc(
            @RequestParam(value = "xa", required = false) Integer xa,
            @RequestParam(value = "quan", required = false) Integer quan,
            @RequestParam(value = "tinh", required = false) Integer tinh,
            @RequestParam(value = "dcCuThe", required = false) String dcCuThe,
            @RequestParam(value = "hoTen", required = false) String hoTen,
            @RequestParam(value = "sdt", required = false) String sdt
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        DiaChi diaChi = new DiaChi();
        DiaChi diaChiMD = diaChiServiceTQ.getDiaChiByIdTaiKhoan(taiKhoan.getId());
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiServiceTQ.saveDC(diaChiMD);
        }
        diaChi.setTaiKhoan(taiKhoan);
        diaChi.setPhuongXa(xa);
        diaChi.setQuanHuyen(quan);
        diaChi.setTinh(tinh);
        diaChi.setDiaChiCuThe(dcCuThe);
        diaChi.setTen(hoTen);
        diaChi.setSdt(sdt);
        diaChi.setDiaChiMacDinh(1);
        diaChiServiceTQ.saveDC(diaChi);
//        alertInfo.alert("successTaiQuay","Địa chỉ đã được thêm");
        return "redirect:/check-out/home";
    }

    @GetMapping("deleteDC/{id}")
    public String delete(
            @PathVariable(value = "id", required = false) String iddc
    ) {
        diaChiServiceTQ.deleteById(iddc);
//        alertInfo.alert("successTaiQuay","Xóa thành công");
        return "redirect:/check-out/home";
    }


    @GetMapping("updateDCMD/{id}")
    public String updateDCMD(
            @PathVariable("id") String iddc
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
        DiaChi diaChiThuong = diaChiServiceTQ.getDiaChiById(iddc);
        DiaChi diaChiMD = diaChiServiceTQ.getDiaChiByIdTaiKhoan(taiKhoan.getId());
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiServiceTQ.saveDC(diaChiMD);
        }
        if (diaChiThuong.getDiaChiMacDinh() == 0) {
            diaChiThuong.setDiaChiMacDinh(1);
            diaChiServiceTQ.saveDC(diaChiThuong);
        }
//        alertInfo.alert("successTaiQuay","Địa chỉ đã được thay đổi");
        return "redirect:/check-out/home";
    }
}
