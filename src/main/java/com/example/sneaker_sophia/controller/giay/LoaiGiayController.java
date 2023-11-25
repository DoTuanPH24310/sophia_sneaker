package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.LoaiGiayRequest;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.service.LoaiGiayService2;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/loaiGiay")
public class LoaiGiayController {
    @Autowired
    private LoaiGiay loaiGiay;

    @Autowired
    private LoaiGiayService2 loaiGiayService2;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "txtSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<LoaiGiay> page = this.loaiGiayService2.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachLoaiGiay", page);
        model.addAttribute("tongLoaiGiay", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("txtSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/loaiGiay/loaiGiay";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", loaiGiay);
        return "admin/loaiGiay/form_loaiGiay";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") LoaiGiayRequest loaiGiayRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/loaiGiay/form_loaiGiay";
        }
        this.loaiGiayService2.add(loaiGiayRequest);
        return "redirect:/admin/loaiGiay/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, LoaiGiay loaiGiay) {
        Optional<LoaiGiay> loaiGiay1 = this.loaiGiayService2.findOne(loaiGiay.getId());
        model.addAttribute("data", loaiGiay1);
        model.addAttribute("action", "/admin/loaiGiay/update/" +loaiGiay.getId());
        return "admin/loaiGiay/form_loaiGiay_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") LoaiGiay loaiGiay, @Valid @ModelAttribute("data") LoaiGiayRequest loaiGiayRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/admin/loaiGiay/update/" +loaiGiay.getId());
            return "admin/loaiGiay/form_loaiGiay_update";
        }
        this.loaiGiayService2.update(loaiGiay.getId(), loaiGiayRequest);
        return "redirect:/admin/loaiGiay/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") LoaiGiay loaiGiay){
        this.loaiGiayService2.delete(loaiGiay.getId());
        return "redirect:/admin/loaiGiay/hien-thi";
    }
}
