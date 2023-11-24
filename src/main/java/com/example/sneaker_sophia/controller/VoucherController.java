package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.request.VoucherReq;
import com.example.sneaker_sophia.service.CTG_KhuyenMaiService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@EnableAsync
@Controller
@RequestMapping("/admin/voucher")
public class VoucherController {

    @Autowired
    private VoucherService voucherService;

    @Resource(name = "anhRepository")
    AnhRepository anhRepository;


    @Autowired
    private GiayService giayService;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @Autowired
    private CTG_KhuyenMaiService ctg_khuyenMaiService;


    public static int checkSession = 0;

    @Async
    @Scheduled(cron = "1 * * * * *")
    public void test() {
        List<Voucher> listUpdate = voucherService.findByTrangThaiNotLike();
        if (listUpdate.size() == 0) {
            return;
        }
        voucherService.jobUpdate(listUpdate);
    }

    @GetMapping("/hien-thi")
    public String hienThi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo, HttpSession session) {
        if (session.getAttribute("mess") != null) {
            if (checkSession != 0) {
                session.removeAttribute("mess");
            }
            checkSession = 1;
        }
        Pageable pageable = PageRequest.of(pageNo, 10);
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
                           @ModelAttribute(value = "data") VoucherDTO vc, HttpSession session) {

        listId = giayService.checkedGiay(listId, model);
        if (!listId.contains("false") && listId.size() > 0) {
            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }

        voucherService.addAttributeModel(model, listId, listIDCTG);
        if (button.equals("button")) {
            if (listIDCTG.size() <= 0 || listIDCTG.contains("false")) {
                model.addAttribute("err", "Vui lòng chọn sản phẩm khuyến mại");
                return "admin/voucher/add";
            }
            if (listIDCTG.size() > 0 && voucherService.validate(vc, model, listIDCTG)) {
                voucherService.saveVoucher(vc, listIDCTG);
                checkSession = 0;
                session.setAttribute("mess", "Khuyến mại đã được thêm");
                return "redirect:/admin/voucher/hien-thi";
            }
        }
        return "admin/voucher/add";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(Model model, @PathVariable("id") Voucher vc) {
        VoucherReq voucherReq = new VoucherReq();
        BeanUtils.copyProperties(vc, voucherReq);
//        voucherReq.setNgayBatDau(LocalDateTime.parse(String.valueOf(vc.getNgayBatDau()), formatter));
        List<String> listIDCTG = ctg_khuyenMaiService.findIdCTG(vc);
        List<ChiTietGiay> listCTG = ctg_khuyenMaiService.findCTG(vc);
        List<String> listId = new ArrayList<>();
        for (ChiTietGiay x : listCTG) {
            listId.add(x.getGiay().getId().toString());
        }

        List<UUID> listG = giayService.finGiayByCTG(chiTietGiayService.convertStringListToUUIDList(listId));
        List<ChiTietGiay> listCTG2 = chiTietGiayService.getCTGByG(listG);
        Map<UUID, String> avtctgMap = new HashMap<>();
        for (ChiTietGiay ctg: listCTG) {
            String avtct = anhRepository.getAnhChinhByIdctg(ctg.getId());
            avtctgMap.put(ctg.getId(),avtct);
        }
        model.addAttribute("avtctgMap",avtctgMap);
        model.addAttribute("listCTG", listCTG2);
        chiTietGiayService.checkCTG = 0;
        if (listIDCTG.size() == listCTG2.size()){
            model.addAttribute("checkAllCTG", true);
            chiTietGiayService.checkCTG = 1;
        }

        model.addAttribute("data", voucherReq);
        voucherService.addAttributeModel(model, listId, listIDCTG);
        return "/admin/voucher/update";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Voucher vc, HttpSession session) {
        if (vc.getTrangThai() == 0) {
            ctg_khuyenMaiService.deleteByIdKM(vc);
            voucherService.delete(vc);
            checkSession = 0;
            session.setAttribute("mess", "Xóa thành công");
            return "redirect:/admin/voucher/hien-thi#table";
        }
        vc.setTrangThai(3);
        checkSession = 0;
        session.setAttribute("mess", "Xóa thành công");
        voucherService.update(vc);
        return "redirect:/admin/voucher/hien-thi#table";
    }


    @PostMapping("/update/{id}")
    public String update(@RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                         @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG,
                         @RequestParam(value = "button", defaultValue = "false") String button,
                         @ModelAttribute(value = "data") VoucherDTO vc, HttpSession session, Model model) {

        listId = giayService.checkedGiay(listId, model);
        if (!listId.contains("false") && listId.size() > 0) {
            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }
        voucherService.addAttributeModel(model, listId, listIDCTG);
        if (button.equals("button")) {
            if (listIDCTG.size() <= 0 || listIDCTG.contains("false")) {
                model.addAttribute("err", "Vui lòng chọn sản phẩm khuyến mại");
                return "admin/voucher/update";
            }
            if (listIDCTG.size() > 0 && voucherService.validate(vc, model, listIDCTG)) {
                voucherService.saveVoucher(vc, listIDCTG);
                checkSession = 0;
                session.setAttribute("mess", "Khuyến mại đã được cập nhật");
                return "redirect:/admin/voucher/hien-thi#table";
            }
        }
        return "admin/voucher/update";
    }

    //  Cho phép xóa khuyến mại đang áp dụng

}

