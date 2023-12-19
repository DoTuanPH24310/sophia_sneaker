package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.KhuyenMaiWebService;
import com.example.sneaker_sophia.validate.AlertInfo;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private AlertInfo alertInfo;
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
            Map<UUID, Boolean> quantityExceededMap = (Map<UUID, Boolean>) model.getAttribute("quantityExceededMap");
            if (quantityExceededMap == null) {
                quantityExceededMap = new HashMap<>();
                model.addAttribute("quantityExceededMap", quantityExceededMap);
            }
            for (GioHangChiTiet cartItem : cartItems) {
                ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();
                khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
                if (chiTietGiay != null) {
                    khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
                    model.addAttribute("maxQuantity_" + chiTietGiay.getId(), chiTietGiay.getSoLuong());
                    if (cartItem.getSoLuong() > chiTietGiay.getSoLuong()) {
                        quantityExceededMap.put(chiTietGiay.getId(), true);
                    }
                }
            }

            double total = 0.0;
            double tongTienGiam = 0.0;
            int soLuongPhieuGiamDaSuDung = 0;
            int soLuongGiam = 0;
            int tongSoLuongGiam = 0;
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
                Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                Map<UUID, Integer> voucherUsageCount = new HashMap<>();

                for (GioHangChiTiet cartItem : cartItems) {
                    double totalCartItemPrice = calculateTotalCartItemPrice(cartItem, voucherUsageCount);
                    cartItemTotalPrices.put(cartItem.getId().getChiTietGiay().getId(), totalCartItemPrice);
                }

                model.addAttribute("cartItemTotalPrices", cartItemTotalPrices);


                model.addAttribute("totalCartPrice", tongTienDonHang);
                model.addAttribute("soLuongSessionGioHang", cartItems.size());
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("isLoggedIn", true);
            }

            return "website/productwebsite/cart";

        } else {
            // Trong phương thức xử lý giỏ hàng của bạn
            Cart cart = (Cart) httpSession.getAttribute("cart");

            if (cart == null || (cart != null && cart.getItems().isEmpty())) {
                return "website/productwebsite/empty-cart";
            } else {
                Map<UUID, Boolean> quantityExceededMap = (Map<UUID, Boolean>) model.getAttribute("quantityExceededMap");
                if (quantityExceededMap == null) {
                    quantityExceededMap = new HashMap<>();
                    model.addAttribute("quantityExceededMap", quantityExceededMap);
                }

                double totalCartPrice = 0.0;
                double totalDiscount = 0.0;

                Map<UUID, Double> cartItemTotalPrices = new HashMap<>();
                Map<UUID, Integer> voucherUsageCount = new HashMap<>();
                for (CartItem cartItem : cart.getItems()) {
                    UUID chiTietGiayId = cartItem.getId();
                    ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(chiTietGiayId).orElse(null);

                    if (chiTietGiay != null) {
                        khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
                        model.addAttribute("maxQuantity_" + chiTietGiay.getId(), chiTietGiay.getSoLuong());

                        if (cartItem.getSoLuong() > chiTietGiay.getSoLuong()) {
                            quantityExceededMap.put(chiTietGiay.getId(), true);
                        }
                        double totalCartItemPrice = calculateTotalCartItemPricec(cartItem, voucherUsageCount);
                        totalCartPrice += totalCartItemPrice;
                        totalDiscount += (cartItem.getGia() * cartItem.getSoLuong()) - totalCartItemPrice;
                        cartItemTotalPrices.put(chiTietGiay.getId(), totalCartItemPrice);
                    }
                }

                model.addAttribute("cartItemTotalPrices", cartItemTotalPrices);
                model.addAttribute("soLuongSessionGioHang", cart.getItems().size());
                model.addAttribute("totalCartPrice", totalCartPrice);
                model.addAttribute("totalDiscount", totalDiscount);
                model.addAttribute("cartItem", cart.getItems());
            }

            return "website/productwebsite/cartSession";

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




    @GetMapping("/get-cart-item-count")
    public ResponseEntity<Long> getCartItemCount(HttpSession session, Authentication authentication) {
        long cartItemCount = 0;

        // Kiểm tra giỏ hàng trong session
        Cart cartSession = (Cart) session.getAttribute("cart");
        cartItemCount += cartSession != null ? cartSession.getItems().stream().mapToLong(CartItem::getSoLuong).count() : 0;

        // Kiểm tra giỏ hàng trong database nếu người dùng đã đăng nhập
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            List<GioHangChiTiet> cartItemsDatabase = cartService.getCartItems(authentication.getName());
            cartItemCount += cartItemsDatabase != null ? cartItemsDatabase.stream().mapToLong(GioHangChiTiet::getSoLuong).count() : 0;
        }

        return ResponseEntity.ok(cartItemCount);
    }


    @GetMapping("/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") UUID chiTietGiayId, Model model,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession httpSession) {
        try {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId).orElse(null);

            if (chiTietGiay != null) {
                int maxQuantity = chiTietGiay.getSoLuong();
                boolean maxQuantityReached = false;

                if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
                    // Người dùng đã đăng nhập
                    TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());
                    cartService.addToCart(taiKhoan.getEmail(), chiTietGiayId, quantity);

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

                    maxQuantityReached = cartItems.stream()
                            .anyMatch(item -> item.getId().getChiTietGiay().getId().equals(chiTietGiayId) && item.getSoLuong() == maxQuantity);
                } else {
                    // Người dùng chưa đăng nhập
                    cartService.addToCartNoLogin(chiTietGiayId, httpSession, quantity);

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

                        maxQuantityReached = cartItems.stream()
                                .anyMatch(item -> item.getId().equals(chiTietGiayId) && item.getSoLuong() == maxQuantity);
                    }
                }

                model.addAttribute("maxQuantityReached", maxQuantityReached);
            }

            return "redirect:/sophia-store/product";
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
            alertInfo.alert("successOnline", "Đã xóa sản phẩm khỏi giỏ hàng.");
            return "redirect:/cart/hien-thi"; // Chuyển hướng về trang giỏ hàng hoặc trang khác
        } catch (Exception e) {
            alertInfo.alert("errOnline", "Đã xóa sản phẩm khỏi giỏ hàng.");
            return "website/productwebsite/cart";
        }
    }


}
