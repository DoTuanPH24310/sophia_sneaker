package com.example.sneaker_sophia.controller;
import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.request.VoucherReq;
import com.example.sneaker_sophia.service.CTG_KhuyenMaiService;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.GiayService;
import com.example.sneaker_sophia.service.VoucherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
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
    private CTG_KhuyenMaiService ctg_khuyenMaiService;


    public static int checkSession = 0;


//    @Scheduled(initialDelay = 10000, fixedDelay = 600000)
//    public void test() {
//        List<Voucher> listUpdate = voucherRepository.findAll();
//        voucherService.jobUpdate(listUpdate);
//    }

    @GetMapping("/hien-thi")
    public String hienThi(Model model, @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo, HttpSession session) {
        if (session.getAttribute("mess") != null) {
            if (checkSession != 0) {
                session.removeAttribute("mess");
            }
            checkSession = 1;
        }
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
        BeanUtils.copyProperties(vc,voucherReq);
        List<String> listIDCTG = ctg_khuyenMaiService.findIdCTG(vc);
        List<ChiTietGiay> listCTG = ctg_khuyenMaiService.findCTG(vc);
        List<String> listId = new ArrayList<>();
        for (ChiTietGiay x: listCTG) {
            listId.add(x.getGiay().getId().toString());
        }
        model.addAttribute("listCTG", listCTG);
        model.addAttribute("checkAllCTG","true");
        chiTietGiayService.checkCTG = 1;
        model.addAttribute("data", voucherReq);
        voucherService.addAttributeModel(model, listId, listIDCTG);
        return "/admin/voucher/update";
    }

//    Thay doi
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Voucher vc) {
        model.addAttribute("data", vc);
        List<ChiTietGiay> listCTG = ctg_khuyenMaiService.findCTG(vc);
        model.addAttribute("listctg",listCTG);
        return "/admin/voucher/detail";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable("id") Voucher vc, HttpSession session) {
//      Nếu trạng thái sắp diễn ra thì xóa luôn
//      Nếu hết hạn thì xóa mềm
        if (vc.getTrangThai() != 0 && vc.getTrangThai() != 2){
            checkSession = 0;
            session.setAttribute("mess","Thao tác không chính xác");
        }

        if (vc.getTrangThai() == 0) {
            ctg_khuyenMaiService.deleteByIdKM(vc);
            voucherService.delete(vc);
            checkSession = 0;
            session.setAttribute("mess","Xóa thành công");
            return "redirect:/admin/voucher/hien-thi";
        }
        vc.setTrangThai(3);
        checkSession = 0;
        session.setAttribute("mess","Xóa thành công");
        voucherService.update(vc);
        return "redirect:/admin/voucher/hien-thi";
    }


    @PostMapping("/update/{id}")
    public String update( @RequestParam(value = "requestId", defaultValue = "false") List<String> listId,
                          @RequestParam(value = "requestIdCTG", defaultValue = "false") List<String> listIDCTG,
                          @RequestParam(value = "button", defaultValue = "false") String button,
                          @ModelAttribute(value = "data") VoucherDTO vc, HttpSession session, Model model ){

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
                return "redirect:/admin/voucher/hien-thi";
            }
        }
        return "admin/voucher/update";
    }

    public void test(){

    }

}

