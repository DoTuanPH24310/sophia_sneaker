package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.KichCoRequest;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.service.KichCoService;
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
@RequestMapping("kichCo")
public class KichCoController {
    @Autowired
    private KichCo kichCo;

    @Autowired
    private KichCoService kichCoService;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "txtSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<KichCo> page = this.kichCoService.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachKichCo", page);
        model.addAttribute("tongKichCo", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("textSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/kichCo/kichCo";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", kichCo);
        return "admin/kichCo/form_kichCo";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") KichCoRequest kichCoRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/form_kichCo";
        }
        this.kichCoService.add(kichCoRequest);
        return "redirect:/kichCo/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, KichCo kichCo) {
        Optional<KichCo> kichCo1 = this.kichCoService.findOne(kichCo.getId());
        model.addAttribute("data", kichCo1);
        model.addAttribute("action", "/kichCo/update/" +kichCo.getId());
        return "admin/kichCo/form_kichCo_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") KichCo kichCo, @Valid @ModelAttribute("data") KichCoRequest kichCoRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/kichCo/update/" +kichCo.getId());
            return "admin/kichCo/form_kichCo_update";
        }
        this.kichCoService.update(kichCo.getId(), kichCoRequest);
        return "redirect:/kichCo/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") KichCo kichCo){
        this.kichCoService.delete(kichCo.getId());
        return "redirect:/kichCo/hien-thi";
    }
}
