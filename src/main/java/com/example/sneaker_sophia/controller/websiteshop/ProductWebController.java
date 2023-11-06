package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
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
    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @GetMapping("/product")
    public String home(Model model,  @RequestParam(value = "giayIds", required = false) List<String> giayTen,
                       @RequestParam(value = "kichCoIds", required = false) List<String> kichCoTen,
                       @RequestParam(value = "deGiayIds", required = false) List<String> deGiayTen,
                       @RequestParam(value = "hangIds", required = false) List<String> hangTen,
                       @RequestParam(value = "loaiGiayIds", required = false) List<String> loaiGiayTen,
                       @RequestParam(value = "mauSacIds", required = false) List<String> mauSacTen,
                       @RequestParam(value = "minPrice", required = false) List<String> minPrice

    ) {
        // Đặt danh sách sản phẩm vào model khi trang ban đầu được tải
        String userEmail = "namdc@gmail.com";
        List<GioHangChiTiet> cartItems = cartService.getCartItems(userEmail);
        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        List<ChiTietGiay> filteredChiTietGiay = chiTietGiayService.filterChiTietGiay(giayTen, kichCoTen, deGiayTen,hangTen, loaiGiayTen, mauSacTen,minPrice);
        Long soLuong = this.cartService.countCartItems(userEmail);

        model.addAttribute("soLuong",soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("danhSachProduct", filteredChiTietGiay);
        model.addAttribute("danhSachHang", this.hangRepository.findAll());
        model.addAttribute("danhSachMauSac", this.mauSacRepository.findAll());
        model.addAttribute("danhSachKichCo", this.kichCoRepository.findAll());
        model.addAttribute("danhSachDeGiay", this.deGiayRepository.findAll());
        model.addAttribute("danhSachLoaiGiay", this.loaiGiayRepository.findAll());
        model.addAttribute("danhSachGiay", this.giayRepository.findAll());
        model.addAttribute("cartItems", cartItems);
        System.out.println("MinPrice values: " + minPrice);
        //giữ giá trị checkbox đã chọn
        model.addAttribute("loaiGiayTen", loaiGiayTen);
        model.addAttribute("hangTen", hangTen);
        model.addAttribute("deGiayTen", deGiayTen);
        model.addAttribute("kichCoTen", kichCoTen);
        model.addAttribute("mauSacTen", mauSacTen);
        model.addAttribute("giayTen", giayTen);
        model.addAttribute("minPrice", minPrice);

        return "website/productwebsite/shop-grid-sidebar-left";
    }


    @GetMapping("them-modal/{id}")
    public String modal(@PathVariable("id") ChiTietGiay giay, Model model){
        Optional<ChiTietGiay> chiTietGiay = this.chiTietGiayRepository.findById(giay.getId());
        model.addAttribute("layGiay", chiTietGiay);
        return "website/productwebsite/shop-grid-sidebar-left";
    }

}


