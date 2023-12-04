package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.HangRequest;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import com.example.sneaker_sophia.service.HangService;
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
@RequestMapping("/admin/hang")
public class HangController {
    @Autowired
    private Hang hang;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private HangRepository hangRepository;
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
        model.addAttribute("txtSearch", txtSearch);
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
        if(result.hasErrors()){
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/hang/form_hang_update";
        }
        if (this.hangRepository.existsHangByMa(hangRequest.getMa())) {
            result.rejectValue("ma", "error.deGiayRequest", "Mã hãng đã tồn tại. Vui lòng chọn mã khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/hang/form_hang_update";
        }

        // Check for existing Giay with the same ten (name)
        if(this.hangRepository.existsHangByTen(hangRequest.getTen())){
            result.rejectValue("ten", "error.deGiayRequest", "Tên hãng đã tồn tại. Vui lòng chọn tên khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/hang/form_hang_update";
        }
        this.hangService.add(hangRequest);
        alertInfo.alert("successTaiQuay", "Đã thêm thành công");
        return "redirect:/admin/hang/hien-thi";
    }

    @GetMapping("view-update/{id}")
    private String viewUpdate(Model model, Hang hang) {
        Optional<Hang> hang1 = this.hangService.findOne(hang.getId());
        model.addAttribute("data", hang1);
        model.addAttribute("action", "/admin/hang/update/" +hang.getId());
        return "admin/hang/form_hang_update";
    }

    @PostMapping("update/{id}")
    private String update(@PathVariable("id") Hang hang, @Valid @ModelAttribute("data") HangRequest hangRequest, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("action", "/admin/hang/update/" +hang.getId());
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/hang/form_hang_update";
        }
        this.hangService.update(hang.getId(), hangRequest);
        alertInfo.alert("successTaiQuay", "Đã sửa thành công");
        return "redirect:/admin/hang/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") Hang hang){
        this.hangService.delete(hang.getId());
        alertInfo.alert("successTaiQuay", "Đã xóa thành công");
        return "redirect:/admin/hang/hien-thi";
    }
}
