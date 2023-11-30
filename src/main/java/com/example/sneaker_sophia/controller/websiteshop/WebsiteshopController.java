package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("sophia-store")
public class
WebsiteshopController {
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @Autowired
    private KhuyenMaiWebService khuyenMaiWebService;
    @Autowired
    GiayService giayService;
    @Autowired
    HangService2 hangService;
    @Autowired
    KichCoService kichCoService;
    @Autowired
    DeGiayService2 deGiayService;
    @Autowired
    MauSacService2 mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @Autowired
    private CartService cartService;
    @Autowired
    HoaDonChiTietServive hoaDonChiTietServive;
    @GetMapping("/home")
    public String home(Model model,HttpSession httpSession){
        List<ChiTietGiay> productList = chiTietGiayService.getAll();
        productList.sort(Comparator.comparing(ChiTietGiay::getNgayTao));
        List<ChiTietGiay> top16Products = productList.subList(0, Math.min(productList.size(), 16));
        model.addAttribute("products", top16Products);

        // cart
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());

        // Tính giá sau khuyến mãi cho từng sản phẩm trong giỏ hàng
        for (GioHangChiTiet cartItem : cartItems) {
            ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();
            khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
        }

        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        Long soLuong = this.cartService.countCartItems(authentication.getName());
        model.addAttribute("soLuong", soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("top10",hoaDonChiTietServive.findTop10IdChiTietGiay());

        return "website/websiteShop/index";
    }


    @GetMapping("/detail/{id}")
    public String Detail(Model model, @PathVariable("id") UUID id){

        model.addAttribute("giay",chiTietGiayService.getOne(id));
        model.addAttribute("kichCo",kichCoService.getAll());
        model.addAttribute("chiTietGiayById",chiTietGiayService.findChiTietGiaysById(id));
        model.addAttribute("size",chiTietGiayService.getChiTietGiaysByIdChiTietGiay(
                giayService.findGiaysByIdChiTietGiay(id),
                deGiayService.findDeGiaysByIdChiTiet(id),
                hangService.findHangsByIdChiTietGiay(id),
                loaiGiayService.findHangsByIdChiTietGiay(id),
                mauSacService.findMauSacsByIdChiTiet(id)
        ));
        // cart
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());
        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        Long soLuong = this.cartService.countCartItems(authentication.getName());
        model.addAttribute("soLuong", soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("cartItems", cartItems);
        return "website/websiteShop/product-details-default";
    }

    @GetMapping("/shop1")
    public String blog(Model model){

        return "website/blog-single-sidebar-left";
    }
}
