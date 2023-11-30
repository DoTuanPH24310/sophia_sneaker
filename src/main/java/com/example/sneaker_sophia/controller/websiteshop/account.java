package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("my-account")
public class account {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DiaChiCheckoutService diaChiService;

    @Resource(name = "hoaDonChiTietServive")
    HoaDonChiTietServive hoaDonChiTietServive;

    @Resource(name = "lshdService")
    LSHDService lshdService;

    @Resource(name = "hoaDonService")
    HoaDonService hoaDonService;

    @Autowired private HoaDonWebRepository hoaDonWebRepository;

    @Resource(name = "anhRepository")
    AnhRepository anhRepository;

    @GetMapping("home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {

            DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();
            Map<String, Double> tongTienMap = new HashMap<>();
            TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());
//        Double tongTien =
            List<HoaDon> listHD = hoaDonWebRepository.findByTaiKhoanAndLoaiHoaDonEqualsOrderByCreatedDateDesc(taiKhoan, 3);
            for (HoaDon hd : listHD) {
                tongTienMap.put(hd.getId(), hoaDonChiTietServive.tongTienSauGiam(hd.getId()));
            }
            List<DiaChi> diaChiList = accountRepository.findByTaiKhoan_Email(authentication.getName());
            if (diaChi != null) {

                session.setAttribute("tinh", diaChi.getTinh());
                session.setAttribute("quan", diaChi.getQuanHuyen());
                session.setAttribute("phuong", diaChi.getPhuongXa());
            } else {
                diaChi = new DiaChi();
            }
            model.addAttribute("account", taiKhoan);
            model.addAttribute("listHD", listHD);
            model.addAttribute("tongTienMap", tongTienMap);
            model.addAttribute("diaChii", diaChi);
            model.addAttribute("diaChiList", diaChiList);
            return "website/productwebsite/my-account";
        }
        return "redirect:/login/home";
    }

    @GetMapping("detail/{idhd}")
    private String detail(@PathVariable("idhd") HoaDon hd, Model model){
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(hd.getId());
        Map<UUID, String> avtctgMap = new HashMap<>();
        for (HoaDonChiTiet hdct : listhdct) {
            UUID idctg = hdct.getChiTietGiay().getId();
            String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
            avtctgMap.put(idctg, avtctg);
        }
        model.addAttribute("lichSuHoaDon", lshdService.getLSHDBYIdhd(hd.getId()));
        model.addAttribute("avtctgMap", avtctgMap);
        model.addAttribute("hoaDon",hd);
        model.addAttribute("listhdct", listhdct);
        return "website/productwebsite/detail-hoa-don";
    }

    @GetMapping("/cancel/{idhd}")
    private String cancel(@PathVariable("idhd") HoaDon hd,
                          @RequestParam(value = "value",required = false) String value){
        if (value == null || value.length() > 150 || hd.getTrangThai() != 3){
            return "/my-account/detail/"+hd.getId();
        }

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();

        lshdService.savelshd(lichSuHoaDon);
        hd.setGhiChu(value);
        hd.setTrangThai(6);
        lichSuHoaDon.setHoaDon(hd);
        lichSuHoaDon.setPhuongThuc("6");
        hoaDonService.savehd(hd);
        return "redirect:/my-account/home";
    }

    @PostMapping("update-account")
    public String updateAccount(@Valid @ModelAttribute("account") TaiKhoanDTO taiKhoanDTO, BindingResult result,
                                HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            if (result.hasErrors()) {
                DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();
                List<DiaChi> diaChiList = accountRepository.findByTaiKhoan_Email(authentication.getName());
                if (diaChi != null) {

                    session.setAttribute("tinh", diaChi.getTinh());
                    session.setAttribute("quan", diaChi.getQuanHuyen());
                    session.setAttribute("phuong", diaChi.getPhuongXa());
                } else {
                    diaChi = new DiaChi();
                }
                model.addAttribute("diaChii", diaChi);
                model.addAttribute("diaChiList", diaChiList);
                return "website/productwebsite/my-account";
            }

            this.accountService.updateTaiKhoan(taiKhoanDTO);

            return "redirect:/my-account/home";
        }
        return "redirect:/login/home";
    }

}
