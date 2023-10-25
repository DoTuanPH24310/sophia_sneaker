package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.DeGiayRequest;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.service.DeGiayService;
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
@RequestMapping("/admin/deGiay")
public class DeGiayController {
    @Autowired
    private DeGiay deGiay;

    @Autowired
    private DeGiayService deGiayService;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "txtSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<DeGiay> page = this.deGiayService.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachDeGiay", page);
        model.addAttribute("tongDeGiay", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("textSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/deGiay/deGiay";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", deGiay);
        return "admin/deGiay/form_deGiay";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") DeGiayRequest deGiayRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/deGiay/form_deGiay";
        }
        this.deGiayService.add(deGiayRequest);
        return "redirect:/admin/deGiay/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, DeGiay deGiay) {
        Optional<DeGiay> deGiay1 = this.deGiayService.findOne(deGiay.getId());
        model.addAttribute("data", deGiay1);
        model.addAttribute("action", "/admin/deGiay/update/" +deGiay.getId());
        return "admin/deGiay/form_deGiay_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") DeGiay deGiay, @Valid @ModelAttribute("data") DeGiayRequest deGiayRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/admin/deGiay/update/" +deGiay.getId());
            return "admin/deGiay/form_deGiay_update";
        }
        this.deGiayService.update(deGiay.getId(), deGiayRequest);
        return "redirect:/admin/deGiay/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") DeGiay deGiay){
        this.deGiayService.delete(deGiay.getId());
        return "redirect:/admin/deGiay/hien-thi";
    }
}
