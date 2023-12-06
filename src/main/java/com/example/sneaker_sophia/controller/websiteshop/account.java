package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import com.example.sneaker_sophia.service.*;
import com.example.sneaker_sophia.validate.AlertInfo;
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
    private HinhThucThanhToanWebRepository hinhThucThanhToanWebRepository;
    @Autowired
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;
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

    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;

    @Resource(name = "anhRepository")
    AnhRepository anhRepository;

    @Resource(name = "htttService")
    HTTTService htttService;


    @Resource(name = "chiTietGiayService")
    ChiTietGiayService chiTietGiayService;

    @Autowired
    private AlertInfo alertInfo;

    @GetMapping("home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            TaiKhoan taiKhoan = loginRepository.findByEmail(authentication.getName());
            DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();
            List<DiaChi> diaChiList = accountRepository.findByTaiKhoan_Email(authentication.getName());
            if (diaChi != null) {

                session.setAttribute("tinh", diaChi.getTinh());
                session.setAttribute("quan", diaChi.getQuanHuyen());
                session.setAttribute("phuong", diaChi.getPhuongXa());
            } else {
                diaChi = new DiaChi();
            }
            model.addAttribute("email", authentication.getName());
            model.addAttribute("account", taiKhoan);
            model.addAttribute("listHDCTT", hoaDonService.findByTrangThaiAndKhachHang(2, authentication.getName()));
            model.addAttribute("listHDCXN", hoaDonService.findByTrangThaiAndKhachHang(3, authentication.getName()));
            model.addAttribute("listHDCG", hoaDonService.findByTrangThaiAndKhachHang(4, authentication.getName()));
            model.addAttribute("listHDDG", hoaDonService.findByTrangThaiAndKhachHang(5, authentication.getName()));
            model.addAttribute("listHDHT", hoaDonService.findByTrangThaiAndKhachHang(1, authentication.getName()));
            model.addAttribute("listHDH", hoaDonService.findByTrangThaiAndKhachHang(6, authentication.getName()));

            model.addAttribute("diaChii", diaChi);
            model.addAttribute("diaChiList", diaChiList);
            return "website/productwebsite/my-account";
        }
        return "redirect:/login/home";
    }

    @GetMapping("detail/{idhd}")
    private String detail(@PathVariable("idhd") HoaDon hd, Model model, HttpSession session) {
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(hd.getId());
        HoaDon hoaDon = this.hoaDonWebRepository.findById(hd.getId()).orElse(null);
        session.setAttribute("maHD", hoaDon.getMaHoaDOn());
        Map<UUID, String> avtctgMap = new HashMap<>();
        for (HoaDonChiTiet hdct : listhdct) {
            UUID idctg = hdct.getChiTietGiay().getId();
            String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
            avtctgMap.put(idctg, avtctg);
        }
        model.addAttribute("lichSuHoaDon", lshdService.getLSHDBYIdhd(hd.getId()));
        model.addAttribute("avtctgMap", avtctgMap);
        model.addAttribute("hoaDon", hd);
        model.addAttribute("listhdct", listhdct);
        HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(hd.getId());
        if (hinhThucThanhToan != null) {
            model.addAttribute("httt", hinhThucThanhToan);
        }
        return "website/productwebsite/detail-hoa-don";
    }

    @GetMapping("/thanh-toan")
    public String thanhToan(@RequestParam(value = "hinhThucThanhToan", required = false) Integer hinhThucThanhToan,
                            @RequestParam(value = "ghiChu", required = false) String ghiChu,
                            Model model, HttpSession session
    ) {
        String mahd = (String) session.getAttribute("maHD");
        HoaDon don = this.hoaDonWebRepository.findByMaHoaDOn(mahd);
        if (hinhThucThanhToan == null) {
            alertInfo.alert("errOnline", "Bạn chưa chọn hình thức thanh toán!");
            return "redirect:/my-account/detail/"+don.getId();
        }

        if (!(hinhThucThanhToan == 1) && !(hinhThucThanhToan == 2)) {
            if (hinhThucThanhToan == 3) {
                don.setTrangThai(3);
            } else if (hinhThucThanhToan == 2) {
                don.setTrangThai(2);
            }
            don.setGhiChu(ghiChu);
            this.hoaDonWebRepository.save(don);
            HinhThucThanhToan hinhThuc = this.hinhThucThanhToanWebRepository.findByHoaDon(don);
            hinhThuc.setTrangThai(hinhThucThanhToan);
            hinhThuc.setHoaDon(don);
            this.hinhThucThanhToanWebRepository.save(hinhThuc);

            LichSuHoaDon lichSuHoaDon = this.lichSuHoaDonWebRepository.findByHoaDon(don);
            lichSuHoaDon.setHoaDon(don);
            if (hinhThucThanhToan == 3) {
                lichSuHoaDon.setPhuongThuc("3");
            } else if (hinhThucThanhToan == 2) {
                lichSuHoaDon.setPhuongThuc("2");
            }
            this.lichSuHoaDonWebRepository.save(lichSuHoaDon);
            return "redirect:/my-account/home";
        }

        return "/my-account/detail/" + don.getId();
    }

    @GetMapping("/cancel/{idhd}")
    private String cancel(@PathVariable("idhd") HoaDon hd,
                          @RequestParam(value = "value",required = false) String value){
        if (value == null || value.length() > 150 || hd.getTrangThai() != 3){
            alertInfo.alert("errTaiQuay", null);
            return "/my-account/detail/"+hd.getId();
        }

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(hd.getId());
        for (HoaDonChiTiet hdct : listhdct) {
            ChiTietGiay chiTietGiay = hdct.getChiTietGiay();
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + hdct.getSoLuong());
            chiTietGiayService.save(chiTietGiay);
        }
        hd.setGhiChu(value);
        hd.setTrangThai(6);
        lichSuHoaDon.setHoaDon(hd);
        lichSuHoaDon.setPhuongThuc("6");
        lshdService.savelshd(lichSuHoaDon);
        hoaDonService.savehd(hd);
        alertInfo.alert("successTaiQuay", "Đơn hàng đã được hủy");
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