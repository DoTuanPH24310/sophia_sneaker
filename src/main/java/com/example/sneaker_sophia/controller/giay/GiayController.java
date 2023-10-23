package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.GiayRequest;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.GiayRepository;
import com.example.sneaker_sophia.service.GiayService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("giay")
public class GiayController {
    @Autowired
    private Giay giay;

    @Autowired
    private GiayService giayService;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "textSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Giay> page = this.giayService.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachGiay", page);
        model.addAttribute("tongGiay", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("textSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/giay";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", giay);
        return "admin/form_giay";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") GiayRequest giayRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/form_giay";
        }
        this.giayService.add(giayRequest);
        return "redirect:/giay/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, Giay giay) {
        Optional<Giay> giay1 = this.giayService.findOne(giay.getId());
        model.addAttribute("data", giay1);
        model.addAttribute("action", "/giay/update/" +giay.getId());
        return "admin/form_giay_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") Giay giay, @Valid @ModelAttribute("data") GiayRequest giayRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/giay/update/" +giay.getId());
            return "admin/form_giay_update";
        }
        this.giayService.update(giay.getId(), giayRequest);
        return "redirect:/giay/hien-thi";
    }

    @DeleteMapping("delete/{id}")
    private String delete(@PathVariable("id") Giay giay){
        this.giayService.delete(giay.getId());
        return "redirect:/giay/hien-thi";
    }

}
