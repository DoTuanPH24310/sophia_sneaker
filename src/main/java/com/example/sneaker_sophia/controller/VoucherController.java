package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
import com.example.sneaker_sophia.util.ListIDGiay;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private GiayService giayService;


    @Autowired
    private HttpSession session;


    @GetMapping("/hien-thi")
    public String hienThi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo) {
        List<Voucher> listVC = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page page = voucherService.locVaTimKiem(pageable, model);
        model.addAttribute("listVC", page);
        model.addAttribute("tongVC", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        return "/admin/voucher/index";
    }


    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        model.addAttribute("listId", new ArrayList<>(List.of("false")));
        model.addAttribute("listIdCTG", new ArrayList<>(List.of("false")));
        model.addAttribute("listGiay", giayService.findAll());
        return "admin/voucher/add";
    }


    @PostMapping("/view-add")
    public String getTable(Model model, @RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                            @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String>listIDCTG) {
        System.out.println(listId.size() + " chek size");
        // khi chọn cả 3
        // Khi chọn nút all rồi lại bỏ chọn
        // Khi chọn All rồi nhưng lại bỏ chọn 1 dòng dữ liệu khác
        listId = giayService.checkedGiay(listId, model);

        model.addAttribute("listId", listId);
        model.addAttribute("listIdCTG",listIDCTG);
        model.addAttribute("listGiay", giayService.findAll());
        return "admin/voucher/add";
    }


}
