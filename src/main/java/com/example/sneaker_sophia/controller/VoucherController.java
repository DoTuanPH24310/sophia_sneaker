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
import com.example.sneaker_sophia.validate.AlertInfo;
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
    private HttpSession session;
    @Autowired
    private GiayService giayService;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @Autowired
    private CTG_KhuyenMaiService ctg_khuyenMaiService;

    @Autowired
    private AlertInfo alertInfo;



    @Async
    @Scheduled(cron = "0 * * * * *")
    public void test() {
        List<Voucher> listUpdate = voucherService.findByTrangThaiNotLike();
        if (listUpdate.size() == 0) {
            return;
        }
        voucherService.jobUpdate(listUpdate);
    }


    @GetMapping("/hien-thi")
    public String hienThi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo, HttpSession session) {
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
                           @ModelAttribute(value = "data") VoucherDTO vc, HttpSession session) {

        listId = giayService.checkedGiay(listId, model,null);
        if (!listId.contains("false") && listId.size() > 0) {

            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }

        voucherService.addAttributeModel(model, listId, listIDCTG);
        if (button.equals("button")) {
            if (listIDCTG.size() <= 0 || listIDCTG.contains("false")) {
                alertInfo.alert("errTaiQuay","Vui lòng chọn sản phẩm khuyến mại");
                return "admin/voucher/add";
            }
            if (listIDCTG.size() > 0 && voucherService.validate(vc, model, listIDCTG)) {
                voucherService.saveVoucher(vc, listIDCTG);
                alertInfo.alert("successTaiQuay","Khuyến mại đã được thêm");
                return "redirect:/admin/voucher/hien-thi";
            }
        }
        return "admin/voucher/add";
    }

    @GetMapping("/view-update/{id}")
    public String viewUpdate(Model model, @PathVariable("id") Voucher vc) {
        if (vc.getTrangThai() != 0) {
            alertInfo.alert("errTaiQuay","Trạng thái khuyến mại thay đổi. Thao tác lại");
            return "redirect:/admin/voucher/hien-thi";
        }
        VoucherReq voucherReq = new VoucherReq();
        BeanUtils.copyProperties(vc, voucherReq);
        List<String> listIDCTG = ctg_khuyenMaiService.findIdCTG(vc);
        List<ChiTietGiay> listCTG = ctg_khuyenMaiService.findCTG(vc);
        List<String> listId = new ArrayList<>();
        for (ChiTietGiay x : listCTG) {
            listId.add(x.getGiay().getId().toString());
        }

        List<UUID> listG = giayService.finGiayByCTG(chiTietGiayService.convertStringListToUUIDList(listId));
        List<ChiTietGiay> listCTG2 = chiTietGiayService.getCTGByG(listG,vc.getId());
        Map<UUID, String> avtctgMap = new HashMap<>();
        for (ChiTietGiay ctg : listCTG) {
            String avtct = anhRepository.getAnhChinhByIdctg(ctg.getId());
            avtctgMap.put(ctg.getId(), avtct);
        }
        model.addAttribute("avtctgMap", avtctgMap);
        model.addAttribute("listCTG", listCTG2);
        chiTietGiayService.checkCTG = 0;
        if (listIDCTG.size() == listCTG2.size()) {
            model.addAttribute("checkAllCTG", true);
            chiTietGiayService.checkCTG = 1;
        }

        model.addAttribute("data", voucherReq);
        voucherService.addAttributeModel(model, listId, listIDCTG);
        return "/admin/voucher/update";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Voucher vc) {
        Map<UUID, String> avtctgMap = new HashMap<>();
        List<ChiTietGiay> listCTG = ctg_khuyenMaiService.findCTG(vc);
        for (ChiTietGiay ctg : listCTG) {
            String avtct = anhRepository.getAnhChinhByIdctg(ctg.getId());
            avtctgMap.put(ctg.getId(), avtct);
        }
        model.addAttribute("avtctgMap", avtctgMap);
        model.addAttribute("listctg",listCTG);
        model.addAttribute("avtctgMap", avtctgMap);
        model.addAttribute("data", vc);
        return "/admin/voucher/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Voucher vc) {
        if (vc.getTrangThai() == 1) {
            alertInfo.alert("errTaiQuay","Trạng thái khuyến mại thay đổi. Thao tác lại");
            return "redirect:/admin/voucher/hien-thi";

        }
        if (vc.getTrangThai() == 0) {
            ctg_khuyenMaiService.deleteByIdKM(vc);
            voucherService.delete(vc);
            alertInfo.alert("successTaiQuay","Khuyến mại đã được xóa");
            return "redirect:/admin/voucher/hien-thi#table";
        }
        vc.setTrangThai(3);
        alertInfo.alert("successTaiQuay","Khuyến mại đã được xóa");
        voucherService.update(vc);
        return "redirect:/admin/voucher/hien-thi#table";
    }


    @PostMapping("/update/{id}")
    public String update(@RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                         @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG,
                         @RequestParam(value = "button", defaultValue = "false") String button,
                         @ModelAttribute(value = "data") VoucherDTO vc, Model model) {

        listId = giayService.checkedGiay(listId, model,vc.getId());
        if (!listId.contains("false") && listId.size() > 0) {
            listIDCTG = chiTietGiayService.checkedCTG(listIDCTG, model, listId);
        }
        voucherService.addAttributeModel(model, listId, listIDCTG);
        if (button.equals("button")) {
            if (listIDCTG.size() <= 0 || listIDCTG.contains("false")) {
                alertInfo.alert("errTaiQuay","Vui lòng chọn sản phẩm khuyến mại");
                return "admin/voucher/update";
            }
            if (listIDCTG.size() > 0 && voucherService.validate(vc, model, listIDCTG)) {
                voucherService.saveVoucher(vc, listIDCTG);
                alertInfo.alert("successTaiQuay","Khuyến mại đã được cập nhật");
                return "redirect:/admin/voucher/hien-thi#table";
            }
        }
        return "admin/voucher/update";
    }

    //  Cho phép xóa khuyến mại đang áp dụng

}

