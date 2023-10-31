package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sophia-store")
public class ProductWebController {

    @Autowired
    private HangRepository hangRepository;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @GetMapping("/product")
    public String home(Model model){
//        model.addAttribute("danhSachHang", this.hangRepository.findAll());
        model.addAttribute("danhSachroduct", this.chiTietGiayRepository.findAll());
        return "website/productwebsite/shop-grid-sidebar-left";
    }
}
