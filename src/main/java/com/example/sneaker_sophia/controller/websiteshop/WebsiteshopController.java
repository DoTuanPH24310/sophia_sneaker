package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WebsiteshopController {
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @Autowired
    GiayService giayService;
    @Autowired
    HangService hangService;
    @Autowired
    KichCoService kichCoService;
    @Autowired
    DeGiayService deGiayService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @GetMapping("/home")
    public String home(Model model){
        List<Giay> productList = giayService.getAll();
        productList.sort(Comparator.comparing(Giay::getId));
        List<Giay> top16Products = productList.subList(0, Math.min(productList.size(), 16));
        model.addAttribute("products", top16Products);

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
        return "website/websiteShop/product-details-default";
    }

    @GetMapping("/shop1")
    public String blog(Model model){

        return "website/blog-single-sidebar-left";
    }
}
