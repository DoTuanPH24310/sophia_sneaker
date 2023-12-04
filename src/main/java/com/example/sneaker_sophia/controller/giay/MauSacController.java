package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.MauSacRequest;
import com.example.sneaker_sophia.entity.MauSac;
import com.example.sneaker_sophia.repository.MauSacRepository;
import com.example.sneaker_sophia.service.MauSacService;
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
@RequestMapping("/admin/mauSac")
public class MauSacController {
    @Autowired
    private MauSac mauSac;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private MauSacRepository mauSacRepository;
    @Autowired
    private MauSacService mauSacService;

    @GetMapping("hien-thi")
    public String hienthi(Model model,
                          @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
                          @RequestParam(name = "txtSearch", required = false) String txtSearch,
                          @RequestParam(name = "trangThai", required = false) String trangThai) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<MauSac> page = this.mauSacService.fillter(txtSearch, trangThai, pageable);
        model.addAttribute("danhSachMauSac", page);
        model.addAttribute("tongMauSac", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("txtSearch", txtSearch);
        model.addAttribute("trangThai", trangThai);

        return "admin/mauSac/mauSac";
    }

    @GetMapping("view-add")
    private String viewAdd(Model model) {
        model.addAttribute("data", mauSac);
        return "admin/mauSac/form_mauSac";
    }

    @PostMapping("add")
    private String add(@Valid @ModelAttribute("data") MauSacRequest mauSacRequest, BindingResult result) {
        if (result.hasErrors()) {
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/mauSac/form_mauSac";
        }

        if (this.mauSacRepository.existsMauSacByMa(mauSacRequest.getMa())) {
            result.rejectValue("ma", "error.mauSacRequest", "Mã màu sắc giày đã tồn tại. Vui lòng chọn mã khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/mauSac/form_mauSac";
        }

        // Check for existing Giay with the same ten (name)
        if(this.mauSacRepository.existsMauSacByTen(mauSacRequest.getTen())){
            result.rejectValue("ten", "error.mauSacRequest", "Tên màu sắc giày đã tồn tại. Vui lòng chọn tên khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/mauSac/form_mauSac";
        }
        this.mauSacService.add(mauSacRequest);
        alertInfo.alert("successTaiQuay", "Đã thêm thành công");
        return "redirect:/admin/mauSac/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, MauSac mauSac) {
        Optional<MauSac> mauSac1 = this.mauSacService.findOne(mauSac.getId());
        model.addAttribute("data", mauSac1);
        model.addAttribute("action", "/admin/mauSac/update/" +mauSac.getId());
        return "admin/mauSac/form_mauSac_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") MauSac mauSac, @Valid @ModelAttribute("data") MauSacRequest mauSacRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/admin/mauSac/update/" +mauSac.getId());
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/mauSac/form_mauSac_update";
        }
        this.mauSacService.update(mauSac.getId(), mauSacRequest);
        alertInfo.alert("successTaiQuay", "Đã sửa thành công");
        return "redirect:/admin/mauSac/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") MauSac mauSac){
        this.mauSacService.delete(mauSac.getId());
        alertInfo.alert("successTaiQuay", "Đã xóa thành công");
        return "redirect:/admin/mauSac/hien-thi";
    }
}
