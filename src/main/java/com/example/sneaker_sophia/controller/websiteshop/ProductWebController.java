package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.CartService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.KhuyenMaiWebService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private KhuyenMaiWebService khuyenMaiWebService;
    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @GetMapping("/product")
    public String home(
            Model model, HttpSession session,
            @RequestParam(value = "giayIds", required = false) List<String> giayTen,
            @RequestParam(value = "kichCoIds", required = false) List<String> kichCoTen,
            @RequestParam(value = "deGiayIds", required = false) List<String> deGiayTen,
            @RequestParam(value = "hangIds", required = false) List<String> hangTen,
            @RequestParam(value = "loaiGiayIds", required = false) List<String> loaiGiayTen,
            @RequestParam(value = "mauSacIds", required = false) List<String> mauSacTen,
            @RequestParam(value = "minPrice", required = false) List<String> minPrice,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "12") int pageSize,
            @RequestParam(value = "sortField", required = false) String sortField
    ) {
        // Đặt danh sách sản phẩm vào model khi trang ban đầu được tải
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());
        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        Long soLuong = this.cartService.countCartItems(authentication.getName());

        // Gọi hàm filterChiTietGiay để lọc và sắp xếp dữ liệu
        Page<ChiTietGiay> filteredChiTietGiay = chiTietGiayService.filterChiTietGiay(
                giayTen, kichCoTen, deGiayTen, hangTen, loaiGiayTen, mauSacTen, minPrice, page, pageSize, sortField
        );

        List<ChiTietGiay> productList = filteredChiTietGiay.getContent();
        for (ChiTietGiay chiTietGiay : productList) {
            this.khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, session);
            model.addAttribute("giaCu_" + chiTietGiay.getId(), session.getAttribute("giaCu"));
            model.addAttribute("giaMoi_" + chiTietGiay.getId(), session.getAttribute("giaMoi"));

            if (chiTietGiay.getSoLuong() == 0) {
                model.addAttribute("hetHang_" + chiTietGiay.getId(), true);
            } else {
                model.addAttribute("hetHang_" + chiTietGiay.getId(), false);
            }
        }


        model.addAttribute("soLuong", soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("danhSachHang", this.hangRepository.findAll());
        model.addAttribute("danhSachMauSac", this.mauSacRepository.findAll());
        model.addAttribute("danhSachKichCo", this.kichCoRepository.findAll());
        model.addAttribute("danhSachDeGiay", this.deGiayRepository.findAll());
        model.addAttribute("danhSachLoaiGiay", this.loaiGiayRepository.findAll());
        model.addAttribute("danhSachGiay", this.giayRepository.findAll());
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("danhSachProduct", productList); // Lấy danh sách sản phẩm từ Page
        model.addAttribute("totalPages", filteredChiTietGiay.getTotalPages()); // Tổng số trang
        model.addAttribute("currentPage", page); // Trang hiện tại
        model.addAttribute("sortField", sortField);
        //giữ giá trị checkbox đã chọn
        model.addAttribute("loaiGiayTen", loaiGiayTen);
        model.addAttribute("hangTen", hangTen);
        model.addAttribute("deGiayTen", deGiayTen);
        model.addAttribute("kichCoTen", kichCoTen);
        model.addAttribute("mauSacTen", mauSacTen);
        model.addAttribute("giayTen", giayTen);
        model.addAttribute("minPrice", minPrice);
        //giữ giá trị combobox đã chọn
        model.addAttribute("selectedSortField", sortField);

        return "website/productwebsite/shop-grid-sidebar-left";
    }


    @GetMapping("them-modal/{id}")
    public String modal(@PathVariable("id") ChiTietGiay giay, Model model){
        Optional<ChiTietGiay> chiTietGiay = this.chiTietGiayRepository.findById(giay.getId());
        model.addAttribute("layGiay", chiTietGiay);
        return "website/productwebsite/shop-grid-sidebar-left";
    }

    @GetMapping("lien-he")
    public String lienHe(){
        return "/website/productwebsite/contact-us";
    }

    @GetMapping("chung-toi")
    public String chungToi(){
        return "/website/productwebsite/about-us";
    }

    @GetMapping("FAQS")
    public String FAQS(){
        return "/website/productwebsite/privacy-policy";
    }



}


