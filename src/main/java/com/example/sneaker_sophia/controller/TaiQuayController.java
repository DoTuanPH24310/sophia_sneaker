package com.example.sneaker_sophia.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tai-quay")
public class TaiQuayController {

    @GetMapping("/hien-thi")
    public String index(Model model){
        return "/admin/taiquay/index";
    }


    @GetMapping("/open-sanpham")
    public String showModal(Model model){
        model.addAttribute("modalSanPham", true);
        return "/admin/taiquay/index";

    }


}
