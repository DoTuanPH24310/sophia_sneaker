package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private GiayService giayService;

    @Autowired
    private ChiTietGiayService chiTietGiayService;


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
        model.addAttribute("listGiay", giayService.findAllByTrangThaiEquals(0));
        return "admin/voucher/add";
    }


    @PostMapping("/view-add")
    public String getTable(Model model, @RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                           @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG) {
//        để so sánh các trường được chọn
        listId = giayService.checkedGiay(listId, model);
        if (listId.contains("false") == false) {
            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }
        System.out.println("Danh sách id của giày: ");
        for (String x: listId) {
            System.out.println(x);
        }

        System.out.println("Danh sách id của chi tiết giày: ");
        for (String x: listIDCTG) {
            System.out.println(x);
        }
        model.addAttribute("listId", listId);
        model.addAttribute("listIDCTG", listIDCTG);
        model.addAttribute("listGiay", giayService.findAllByTrangThaiEquals(0));
        return "admin/voucher/add";
    }


}
