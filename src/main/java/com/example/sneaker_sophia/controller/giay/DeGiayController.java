package com.example.sneaker_sophia.controller.giay;

import com.example.sneaker_sophia.dto.DeGiayRequest;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import com.example.sneaker_sophia.service.DeGiayService;
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
@RequestMapping("/admin/deGiay")
public class DeGiayController {
    @Autowired
    private DeGiay deGiay;
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private DeGiayRepository deGiayRepository;
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
        model.addAttribute("txtSearch", txtSearch);
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
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/deGiay/form_deGiay";
        }

        // Check for existing Giay with the same ma (code)
        if (this.deGiayRepository.existsDeGiayByMa(deGiayRequest.getMa())) {
            result.rejectValue("ma", "error.deGiayRequest", "Mã đế giày đã tồn tại. Vui lòng chọn mã khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/deGiay/form_deGiay";
        }

        // Check for existing Giay with the same ten (name)
        if (this.deGiayRepository.existsDeGiayByTen(deGiayRequest.getTen())) {
            result.rejectValue("ten", "error.deGiayRequest", "Tên đế giày đã tồn tại. Vui lòng chọn tên khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/deGiay/form_deGiay";
        }
        this.deGiayService.add(deGiayRequest);
        alertInfo.alert("successTaiQuay", "Đã thêm thành công");
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
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/deGiay/form_deGiay_update";
        }
//        if (this.deGiayRepository.existsDeGiayByMa(deGiayRequest.getMa())) {
//            result.rejectValue("ma", "error.deGiayRequest", "Mã đế giày đã tồn tại. Vui lòng chọn mã khác.");
//            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
//            return "admin/deGiay/form_deGiay";
//        }

        DeGiay existingDeGiayWithSameTen = this.deGiayRepository.findByTen(deGiayRequest.getTen());
        if (existingDeGiayWithSameTen != null && !existingDeGiayWithSameTen.getId().equals(deGiay.getId())) {
            // Ensure that the existing DeGiay found has a different ID than the one being updated
            result.rejectValue("ten", "error.deGiayRequest", "Tên đế giày đã tồn tại. Vui lòng chọn tên khác.");
            alertInfo.alert("errTaiQuay", "Dữ liệu sai hoặc thiếu");
            return "admin/deGiay/form_deGiay";
        }
        this.deGiayService.update(deGiay.getId(), deGiayRequest);
        alertInfo.alert("successTaiQuay", "Đã sửa thành công");
        return "redirect:/admin/deGiay/hien-thi";
    }

    @GetMapping("delete/{id}")
    private String delete(@PathVariable("id") DeGiay deGiay){
        this.deGiayService.delete(deGiay.getId());
        alertInfo.alert("successTaiQuay", "Đã xóa thành công");
        return "redirect:/admin/deGiay/hien-thi";
    }
}
