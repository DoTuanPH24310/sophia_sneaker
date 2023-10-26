package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/tai-quay")
public class TaiQuayController {
    // trạnh thái = 2 (chờ)
    @Resource(name = "")
    HoaDonService hoaDonService;

    @GetMapping("/hien-thi")
    public String index(Model model) {
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "/admin/taiquay/index";
    }


    @GetMapping("/open-sanpham")
    public String showModal(Model model) {
        model.addAttribute("modalSanPham", true);
        return "/admin/taiquay/index";

    }

    @RequestMapping("/addHD")
    public String addHD(

    ) {

        return "redirect:/admin/tai-quay/hien-thi";
    }

}
