package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.HangRequest;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.service.HangService;
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
@RequestMapping("hang")
public class HangController {
    @Autowired
    private Hang hang;

    @Autowired
    private HangService hangService;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "txtSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Hang> page = this.hangService.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachHang", page);
        model.addAttribute("tongHang", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("textSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/hang/hang";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", hang);
        return "admin/hang/form_hang";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") HangRequest hangRequest, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/hang/form_hang";
        }
        this.hangService.add(hangRequest);
        return "redirect:/hang/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, Hang hang) {
        Optional<Hang> hang1 = this.hangService.findOne(hang.getId());
        model.addAttribute("data", hang1);
        model.addAttribute("action", "/hang/update/" +hang.getId());
        return "admin/hang/form_hang_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") Hang hang, @Valid @ModelAttribute("data") HangRequest hangRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/hang/update/" +hang.getId());
            return "admin/hang/form_hang_update";
        }
        this.hangService.update(hang.getId(), hangRequest);
        return "redirect:/hang/hien-thi";
    }

    @DeleteMapping("delete/{id}")
    private String delete(@PathVariable("id") Hang hang){
        this.hangService.delete(hang.getId());
        return "redirect:/hang/hien-thi";
    }
}
