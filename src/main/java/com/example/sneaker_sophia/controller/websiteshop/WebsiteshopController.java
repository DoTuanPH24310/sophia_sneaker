package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("sophia-store")
public class WebsiteshopController {
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @GetMapping("/home")
    public String home(Model model){
        List<ChiTietGiay> productList = chiTietGiayService.getAll();

        // Sắp xếp danh sách sản phẩm theo yêu cầu (ví dụ: theo ID tăng dần)
        productList.sort(Comparator.comparing(ChiTietGiay::getId));

        // Chỉ lấy 16 phần tử đầu tiên
        List<ChiTietGiay> top16Products = productList.subList(0, Math.min(productList.size(), 16));

        model.addAttribute("products", top16Products);

        return "website/websiteShop/index";
    }

    @GetMapping("/shop")
    public String shop(Model model){

        return "website/shop-grid-sidebar-left";
    }

    @GetMapping("/shop1")
    public String blog(Model model){

        return "website/blog-single-sidebar-left";
    }
}
