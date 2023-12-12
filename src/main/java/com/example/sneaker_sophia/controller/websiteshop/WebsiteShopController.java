package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.MauSac;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("sophia-store")
public class
WebsiteShopController {
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
    KichCoService2 kichCoService2;
    @Autowired
    DeGiayService2 deGiayService;
    @Autowired
    MauSacService2 mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartService cartService;
    @Autowired
    HoaDonChiTietServive hoaDonChiTietServive;

    @GetMapping("/home")
    public String home(Model model, HttpSession httpSession) {
        List<ChiTietGiay> productList = chiTietGiayService.getAll();
        productList.sort(Comparator.comparing(ChiTietGiay::getNgaySua));
        List<ChiTietGiay> top16Products = productList.subList(0, Math.min(productList.size(), 16));
        model.addAttribute("products", top16Products);

        // cart
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GioHangChiTiet> cartItems = cartService.getCartItems(authentication.getName());

        // Tính giá sau khuyến mãi cho từng sản phẩm trong giỏ hàng
        for (ChiTietGiay chiTietGiay : productList) {
            this.khuyenMaiWebService.tinhGiaSauKhuyenMai(chiTietGiay, httpSession);
            model.addAttribute("giaCu_" + chiTietGiay.getId(), httpSession.getAttribute("giaCu"));
            model.addAttribute("giaMoi_" + chiTietGiay.getId(), httpSession.getAttribute("giaMoi"));
        }

        List<ChiTietGiay> chiTietGiays = this.productService.getTop10BestSelling();

        double totalCartPrice = cartItems.stream()
                .mapToDouble(item -> item.getId().getChiTietGiay().getGia() * item.getSoLuong())
                .sum();
        Long soLuong = this.cartService.countCartItems(authentication.getName());
        model.addAttribute("soLuong", soLuong);
        model.addAttribute("totalCartPrice", totalCartPrice);
        model.addAttribute("chiTietGiays", chiTietGiays);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("top10", hoaDonChiTietServive.findTop10IdChiTietGiay());

        return "website/websiteShop/index";
    }


    @GetMapping("/detail/{id}")
    public String Detail(Model model, @PathVariable("id") ChiTietGiay chiTietGiay, HttpSession session) {
        List<KichCo> sizeChiTietGiay = chiTietGiayService.findSimilarSizeChiTietGiay(
                giayService.findGiaysByIdChiTietGiay(chiTietGiay.getId()),
                deGiayService.findDeGiaysByIdChiTiet(chiTietGiay.getId()),
                hangService.findHangsByIdChiTietGiay(chiTietGiay.getId()),
                loaiGiayService.findHangsByIdChiTietGiay(chiTietGiay.getId()),
                mauSacService.findMauSacsByIdChiTiet(chiTietGiay.getId())
        );
        List<MauSac> mauSacChiTietGiay = chiTietGiayService.findSimilarMauSacChiTietGiay(
                giayService.findGiaysByIdChiTietGiay(chiTietGiay.getId()),
                deGiayService.findDeGiaysByIdChiTiet(chiTietGiay.getId()),
                hangService.findHangsByIdChiTietGiay(chiTietGiay.getId()),
                loaiGiayService.findHangsByIdChiTietGiay(chiTietGiay.getId()),
                kichCoService2.findKichCosByIdChiTietGiay(chiTietGiay.getId())
        );
        session.setAttribute("giay", chiTietGiay.getId());

        List<KichCo> distinctSizeChiTietGiay = sizeChiTietGiay.stream()
                .distinct()
                .collect(Collectors.toList());

        List<MauSac> distinctMauSacChiTietGiay = mauSacChiTietGiay.stream()
                .distinct()
                .collect(Collectors.toList());

        model.addAttribute("giay", chiTietGiayService.getOne(chiTietGiay.getId()));
        model.addAttribute("chiTietGiayById", chiTietGiayService.findChiTietGiaysById(chiTietGiay.getId()));
        model.addAttribute("size", distinctSizeChiTietGiay);
        model.addAttribute("mauSac", distinctMauSacChiTietGiay);
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


    @GetMapping("/detaill/{id}")
    public String DetailChiTietGiayTheoSize(@PathVariable("id") UUID id, HttpSession session
    ) {
        UUID idCTG = (UUID) session.getAttribute("giay");
        List<ChiTietGiay> fillChiTietGiayBySize = chiTietGiayService.findSimilarChiTietGiay(
                giayService.findGiaysByIdChiTietGiay(idCTG),
                deGiayService.findDeGiaysByIdChiTiet(idCTG),
                hangService.findHangsByIdChiTietGiay(idCTG),
                loaiGiayService.findHangsByIdChiTietGiay(idCTG),
                mauSacService.findMauSacsByIdChiTiet(idCTG),
                id
        );
        return "redirect:/sophia-store/detail/" +fillChiTietGiayBySize.get(0).getId();
    }

    @GetMapping("/detailll/{id}")
    public String DetailChiTietGiayTheoMauSac(@PathVariable("id") UUID id, HttpSession session
    ) {
        UUID idCTG = (UUID) session.getAttribute("giay");
        List<ChiTietGiay> fillChiTietGiayByMauSac = chiTietGiayService.findSimilarChiTietGiayByMauSac(
                giayService.findGiaysByIdChiTietGiay(idCTG),
                deGiayService.findDeGiaysByIdChiTiet(idCTG),
                hangService.findHangsByIdChiTietGiay(idCTG),
                loaiGiayService.findHangsByIdChiTietGiay(idCTG),
                kichCoService2.findKichCosByIdChiTietGiay(idCTG),
                id
        );
        return "redirect:/sophia-store/detail/" +fillChiTietGiayByMauSac.get(0).getId();
    }
}
