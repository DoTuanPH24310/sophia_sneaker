package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("sophia-store")
public class ProductWebController {

    @Autowired
    private CartService cartService;
    @Autowired
    private GiayRepository giayRepository;
    @Autowired
    private LoaiGiayRepository loaiGiayRepository;
    @Autowired
    private DeGiayRepository deGiayRepository;
    @Autowired
    private KichCoRepository kichCoRepository;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private HangRepository hangRepository;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @GetMapping("/product")
    public String home(Model model){
        // Đặt danh sách sản phẩm vào model khi trang ban đầu được tải
        String userEmail = "namdc@gmail.com";
        List<GioHangChiTiet> cartItems = cartService.getCartItems(userEmail);
        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        List<ChiTietGiay> allProducts = this.chiTietGiayRepository.findAll();
        Long soLuong = this.cartService.countCartItems(userEmail);
        model.addAttribute("soLuong",soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("danhSachProduct", allProducts);
        model.addAttribute("danhSachHang", this.hangRepository.findAll());
        model.addAttribute("danhSachMauSac", this.mauSacRepository.findAll());
        model.addAttribute("danhSachKichCo", this.kichCoRepository.findAll());
        model.addAttribute("danhSachDeGiay", this.deGiayRepository.findAll());
        model.addAttribute("danhSachLoaiGiay", this.loaiGiayRepository.findAll());
        model.addAttribute("danhSachGiay", this.giayRepository.findAll());
        model.addAttribute("cartItems", cartItems);
        return "website/productwebsite/shop-grid-sidebar-left";
    }


    @GetMapping("them-modal/{id}")
    public String modal(@PathVariable("id") ChiTietGiay giay, Model model){
        Optional<ChiTietGiay> chiTietGiay = this.chiTietGiayRepository.findById(giay.getId());
        model.addAttribute("layGiay", chiTietGiay);
        return "website/productwebsite/shop-grid-sidebar-left";
    }

}


