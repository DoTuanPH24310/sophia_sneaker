package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

     static List<String> tempListId ;

     static List<String> tempListIdCTG ;


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
        model.addAttribute("data", new VoucherDTO());
        voucherService.addAttributeModel(model, new ArrayList<>(List.of("false")), new ArrayList<>(List.of("false")));
        return "admin/voucher/add";
    }


    @PostMapping("/view-add")
    public String getTable(Model model, @RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                           @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG) {
        this.tempListId = listId;
        this.tempListIdCTG = listIDCTG;
//        để so sánh các trường được chọn
        model.addAttribute("data", new VoucherDTO());
        System.out.println(listId.size() + "Size 0.1");
        System.out.println( this.tempListId.size() + "Size 0.2");
        listId = giayService.checkedGiay(listId, model);
//        Nếu listId mà chưa được chọn thì mới check listIDCT(không cho chọn chekbox ctg)
        if (listId.contains("false") == false) {
            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }

        voucherService.addAttributeModel(model, listId, listIDCTG);
        return "admin/voucher/add";
    }


    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("data") VoucherDTO vc,
                      BindingResult result, Model model) {
        System.out.println(this.tempListId.size() + "Size 1.1");
        if (result.hasErrors()) {
            System.out.println(this.tempListId.size() + "Size 1.1");
            tempListId = giayService.checkedGiay(tempListId, model);
            System.out.println(tempListId.size() + "Size 1.2");

            if (tempListId.contains("false") == false) {
                tempListIdCTG = chiTietGiayService.checkedCTG(tempListIdCTG, model, tempListId);
            }
            voucherService.addAttributeModel(model, tempListId, tempListIdCTG);
            return "admin/voucher/add";
        }

        return "redirect:/admin/voucher/index";
    }


}
