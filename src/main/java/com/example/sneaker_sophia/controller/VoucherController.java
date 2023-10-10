package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.service.VoucherService;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private ServletRequest request;

    @GetMapping("/hienthi")
    public String hienthi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo) {
        List<Voucher> listVC = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page page = locVaTimKiem(pageable, model);
        model.addAttribute("listVC", page);
        model.addAttribute("tongVC",page.getTotalElements());
        model.addAttribute("pageNo",pageNo);
        return "/admin/voucher/index";
    }


    public Page locVaTimKiem(Pageable pageable, Model model) {
        Page page = null;
        String txtSearch = request.getParameter("textSearch");
        String trangThaiReq = request.getParameter("trangThai");
        String trangThai = trangThaiReq == null || (trangThaiReq.equals("0") == false && trangThaiReq.equals("1") == false) ? "-1" : trangThaiReq;
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("textSearch", txtSearch);

        if (txtSearch == null || txtSearch.trim().length() == 0) {
            if (trangThai.equals("-1")) {
                return voucherService.findAll(pageable);
            }

            Voucher voucher = Voucher.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();


            page = voucherService.findAll(Example.of(voucher), pageable);


        } else {
            if (trangThai.equals("-1")) {
                return voucherRepository.searchAndFilter(txtSearch, null, pageable);
            }
            page = voucherRepository.searchAndFilter(txtSearch, trangThai, pageable);
        }

        return page;
    }
}
