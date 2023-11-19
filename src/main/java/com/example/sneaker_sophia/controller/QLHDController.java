package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/hoa-don")
public class QLHDController {

    @Resource(name = "hoaDonService")
    HoaDonService hoaDonService;

    @GetMapping("/hien-thi")
    public String hienthi(
            Model model
    ){
        List<HoaDon> getAllHDC = hoaDonService.getAllHDC();
        List<HoaDon> getAllHDHT = hoaDonService.getAllHDHT();

        model.addAttribute("listhdc", getAllHDC);
        model.addAttribute("listhdht", getAllHDHT);
        return "admin/hoadon/indexhd";
    }
}
