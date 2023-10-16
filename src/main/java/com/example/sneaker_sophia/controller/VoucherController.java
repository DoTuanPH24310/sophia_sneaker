package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
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

    @GetMapping("/hien-thi")
    public String hienThi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page page = voucherService.locVaTimKiem(pageable, model);
        model.addAttribute("listVC", page);
        model.addAttribute("tongVC", page.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        return "/admin/voucher/index";
    }


    @GetMapping("/view-add")
    public String viewAdd(Model model) {
        giayService.check = 0;
        chiTietGiayService.checkCTG = 0;
        model.addAttribute("data", new VoucherDTO());
        voucherService.addAttributeModel(model, new ArrayList<>(List.of("false")), new ArrayList<>(List.of("false")));
        return "admin/voucher/add";
    }


    @PostMapping("/view-add")
    public String getTable(Model model, @RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                           @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG,
                           @RequestParam(value = "button", defaultValue = "false") String button,
                           @ModelAttribute(value = "data") VoucherDTO vc ) {
        listId = giayService.checkedGiay(listId, model);

        //        Nếu listId mà đã được chọn thì mới check listIDCT(không cho chọn chekbox ctg)
        if (!listId.contains("false") && listId.size()>0) {
                listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }
        voucherService.addAttributeModel(model, listId, listIDCTG);

//        check lại dữ liệu này
        if (button.equals("button") && listId.contains("false")) {
            model.addAttribute("err", "Vui lòng chọn sản phẩm");
            return "admin/voucher/add";
        }
        if (button.equals("button") && voucherService.validate()) {
//            thực hiện thêm vào db
           return "redirect:/admin/voucher/hien-thi";
        }

        return "admin/voucher/add";
    }


}
