package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.KichCoRequest;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.repository.KichCoRepository;
import com.example.sneaker_sophia.service.KichCoService;
import com.example.sneaker_sophia.validate.AlertInfo;
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
@RequestMapping("/admin/kichCo")
public class KichCoController {
    @Autowired
    private KichCo kichCo;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private KichCoRepository kichCoRepository;
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
        model.addAttribute("txtSearch", txtSearch);
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
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/kichCo/form_kichCo";
        }
        if (this.kichCoRepository.existsKichCoByMa(kichCoRequest.getMa())) {
            result.rejectValue("ma", "error.kichCoRequest", "Mã kích cỡ đã tồn tại. Vui lòng chọn mã khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/kichCo/form_kichCo";
        }

        // Check for existing Giay with the same ten (name)
        if(this.kichCoRepository.existsKichCoByTen(kichCoRequest.getTen())){
            result.rejectValue("ten", "error.kichCoRequest", "Tên kích cỡ đã tồn tại. Vui lòng chọn tên khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/kichCo/form_kichCo";
        }
        this.kichCoService.add(kichCoRequest);
        alertInfo.alert("successTaiQuay", "Đã thêm thành công");
        return "redirect:/admin/kichCo/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, KichCo kichCo) {
        Optional<KichCo> kichCo1 = this.kichCoService.findOne(kichCo.getId());
        model.addAttribute("data", kichCo1);
        model.addAttribute("action", "/admin/kichCo/update/" +kichCo.getId());
        return "admin/kichCo/form_kichCo_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") KichCo kichCo, @Valid @ModelAttribute("data") KichCoRequest kichCoRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/admin/kichCo/update/" +kichCo.getId());
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/kichCo/form_kichCo_update";
        }
        this.kichCoService.update(kichCo.getId(), kichCoRequest);
        alertInfo.alert("successTaiQuay", "Đã sửa thành công");
        return "redirect:/admin/kichCo/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") KichCo kichCo){
        this.kichCoService.delete(kichCo.getId());
        alertInfo.alert("successTaiQuay", "Đã xóa thành công");

        return "redirect:/admin/kichCo/hien-thi";
    }

//    -----
}
