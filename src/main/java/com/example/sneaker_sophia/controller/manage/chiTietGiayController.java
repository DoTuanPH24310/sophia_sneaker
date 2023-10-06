package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.service.*;
import com.example.sneaker_sophia.service.iplm.ChiTietGiayIplm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/")
public class chiTietGiayController {

    @Autowired
    ChiTietGiayService chiTietGiayService;
    @Autowired
    GiayService giayService;
    @Autowired
    HangService hangService;
    @Autowired
    DeGiayService deGiayService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @Autowired
    KichCoService kichCoService;


    @Autowired
    HttpServletRequest request;

    @GetMapping("chi-tiet-giay")
    public String index(Model model) {
        List<ChiTietGiay> listChiTietGiay = chiTietGiayService.getAll();
        model.addAttribute("listChiTietGiay", listChiTietGiay);
        return "admin/chiTietGiay/chiTietGiay";
    }
    @GetMapping("chi-tiet-giay/add")
    public String formAdd(Model model) {
        ChiTietGiay chiTietGiay = new ChiTietGiay();
        model.addAttribute("chiTietGiay", chiTietGiay);
        model.addAttribute("giay", giayService.getAll());
        model.addAttribute("hang", hangService.getAll());
        model.addAttribute("deGiay", deGiayService.getAll());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("loaiGiay", loaiGiayService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());

        return "admin/chiTietGiay/formChiTietGiay";
    }

    @GetMapping("chi-tiet-giay/edit/{id}")
    public String edit(Model model, @PathVariable("id")UUID id) {
        ChiTietGiay chiTietGiay = chiTietGiayService.getOne(id);
        model.addAttribute("chiTietGiay", chiTietGiay);
        model.addAttribute("giay", giayService.getAll());
        model.addAttribute("hang", hangService.getAll());
        model.addAttribute("deGiay", deGiayService.getAll());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("loaiGiay", loaiGiayService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());

        return "admin/chiTietGiay/formEditChiTietGiay";
    }

    @PostMapping("chi-tiet-giay/save")
    public String add(ChiTietGiay chiTietGiay) {
        chiTietGiayService.save(chiTietGiay);
        return "redirect:/admin/chi-tiet-giay";
    }
    @PostMapping("chi-tiet-giay/update")
    public String update(ChiTietGiay chiTietGiay) {
        chiTietGiayService.save(chiTietGiay);
        return "redirect:/admin/chi-tiet-giay";
    }

    @GetMapping("chi-tiet-giay/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        chiTietGiayService.delete(id);
        return "redirect:/admin/chi-tiet-giay";
    }
}
