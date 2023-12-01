package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.entity.LichSuHoaDon;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.service.HTTTService;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import com.example.sneaker_sophia.service.LSHDService;
import com.example.sneaker_sophia.validate.AlertInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/hoa-don")
public class QLHDController {
    public static String tempIdHD = "";
    @Resource(name = "hoaDonService")
    HoaDonService hoaDonService;

    @Resource(name = "anhRepository")
    AnhRepository anhRepository;

    @Resource(name = "hoaDonChiTietServive")
    HoaDonChiTietServive hoaDonChiTietServive;

    @Resource(name = "htttService")
    HTTTService htttService;

    @Resource(name = "lshdService")
    LSHDService lshdService;

    @Autowired
    HttpSession session;


    @Autowired
    private AlertInfo alertInfo;


    @GetMapping("/hien-thi")
    public String hienthi(
            Model model
    ) {
        List<HoaDon> getAllHDC = hoaDonService.getAllHDC();
        List<HoaDon> getAllHDHT = hoaDonService.getAllHDHT();
        List<HoaDon> getAllHDChoGiao = hoaDonService.getAllHDChoGiao();
        List<HoaDon> getAllHDDangGiao = hoaDonService.getAllHDDangGiao();
        List<HoaDon> getAllHDHuy = hoaDonService.getAllHDHuy();
        List<HoaDon> getAllHDChoXacNhan = hoaDonService.getAllHDChoXacNhan();
        Integer soHDCG = hoaDonService.soHDCG();
        Integer soHDCXN = hoaDonService.soHDCXN();
        Integer soHDDG = hoaDonService.soHDDG();

        model.addAttribute("listhdc", getAllHDC);
        model.addAttribute("listhdht", getAllHDHT);
        model.addAttribute("listhdcg", getAllHDChoGiao);
        model.addAttribute("listhddg", getAllHDDangGiao);
        model.addAttribute("listhdh", getAllHDHuy);
        model.addAttribute("listhdhcxn", getAllHDChoXacNhan);
        model.addAttribute("soHDCG", soHDCG);
        model.addAttribute("soHDCXN", soHDCXN);
        model.addAttribute("soHDDG", soHDDG);
        return "admin/hoadon/indexhd";
    }

    @GetMapping("detail/{id}")
    public String detail(
            @PathVariable(value = "id", required = false) String idhd,
            Model model
    ) {
        tempIdHD = idhd;
        try {
            UUID uuid = UUID.fromString(idhd);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/hoa-don/hien-thi";
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null) {
            List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(idhd);
            Map<UUID, String> avtctgMap = new HashMap<>();
            for (HoaDonChiTiet hdct : listhdct) {
                UUID idctg = hdct.getChiTietGiay().getId();
                String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
                avtctgMap.put(idctg, avtctg);
            }
            model.addAttribute("avtctgMap", avtctgMap);
            HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(idhd);
            if (hinhThucThanhToan != null) {
                model.addAttribute("httt", hinhThucThanhToan);
            }
            model.addAttribute("displayTable", hinhThucThanhToan != null);
            model.addAttribute("listhdct", listhdct);
            model.addAttribute("hoaDon", hoaDon);
            session.setAttribute("lichSuHoaDon", lshdService.getLSHDBYIdhd(idhd));
            return "admin/hoadon/detailhd";
        } else {
            return "redirect:/admin/hoa-don/hien-thi";
        }

    }

    @PostMapping("updatehdcxn")
    public String updatehdcxn(
            @RequestParam(value = "idhd", required = false) List<String> listhd
    ) {
        session.setAttribute("tabActive", "tabChoXacNhan");
        if (listhd == null) {
            return "redirect:/admin/hoa-don/hien-thi";
        }
        for (String idhd : listhd) {
            try {
                UUID uuid = UUID.fromString(idhd);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin/hoa-don/detail/" + tempIdHD;
            }

            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            if (hoaDon != null) {
                hoaDon.setTrangThai(4);
                LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                lichSuHoaDon.setHoaDon(hoaDon);
                lichSuHoaDon.setPhuongThuc("4");
                lshdService.savelshd(lichSuHoaDon);
                hoaDonService.savehd(hoaDon);
            } else {
                return "redirect:/admin/hoa-don/hien-thi";
            }

        }
        alertInfo.alert("successTaiQuay","Đơn hàng đã được xác nhận");
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehdcg")
    public String updatehdcg(
            @RequestParam(value = "idhd", required = false) List<String> listhdcg, Model model
    ) {
        session.setAttribute("tabActive", "tabChoGiao");
        if (listhdcg == null) {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/hoa-don/hien-thi";
        }
        for (String idhd : listhdcg) {
            try {
                UUID uuid = UUID.fromString(idhd);
            } catch (IllegalArgumentException e) {
                alertInfo.alert("errTaiQuay",null);
                return "redirect:/admin/hoa-don/hien-thi";
            }
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            if (hoaDon != null) {
                List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(hoaDon.getId());
                if(listhdct.size() == 0){
                    return "redirect:/admin/hoa-don/hien-thi";
                }
                hoaDon.setTrangThai(5);
                LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                lichSuHoaDon.setHoaDon(hoaDon);
                lichSuHoaDon.setPhuongThuc("5");
                lshdService.savelshd(lichSuHoaDon);
                hoaDonService.savehd(hoaDon);
            } else {
                alertInfo.alert("errTaiQuay",null);
                return "redirect:/admin/hoa-don/hien-thi";
            }
        }
        alertInfo.alert("successTaiQuay","Đơn hàng đã được giao");
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehddg")
    public String updatehddg(
            @RequestParam(value = "idhd", required = false) List<String> listhddg,
            @RequestParam("value") String ghiChu

    ) {
        session.setAttribute("tabActive", "tabDangGiao");

        if (listhddg == null) {
            return "redirect:/admin/hoa-don/hien-thi";
        }
        if (ghiChu == null) {
            ghiChu = "";
        }else if (ghiChu.length() > 20){
            alertInfo.alert("errTaiQuay","Tối đa 20 kí tự");
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        for (String idhd : listhddg) {
            try {
                UUID uuid = UUID.fromString(idhd);
            } catch (IllegalArgumentException e) {
                return "redirect:/admin/hoa-don/detail/" + tempIdHD;
            }
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            if (hoaDon != null) {
                HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(idhd);
                if (hoaDon.getLoaiHoaDon() == 2) {
                    hinhThucThanhToan.setSoTien(hoaDon.getTongTien());
                    hoaDon.setGhiChu(ghiChu);
                    htttService.savehttt(hinhThucThanhToan);
                }
                hoaDon.setTrangThai(1);
                LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
                lichSuHoaDon.setHoaDon(hoaDon);
                lichSuHoaDon.setPhuongThuc("1");
                lshdService.savelshd(lichSuHoaDon);
                hoaDonService.savehd(hoaDon);
            } else {
                return "redirect:/admin/hoa-don/hien-thi";
            }

        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @GetMapping("updatehdcxn/{id}")
    public String updatehdcxnd(
            @PathVariable(value = "id", required = false) String idhd
    ) {
        try {
            UUID uuid = UUID.fromString(idhd);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        session.setAttribute("tabActive", "tabChoXacNhan");
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null) {
            hoaDon.setTrangThai(4);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("4");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        } else {
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    @GetMapping("updatehdcg/{id}")
    public String updatehdcg(
            @PathVariable(value = "id", required = false) String idhd
    ) {
        try {
            UUID uuid = UUID.fromString(idhd);
        } catch (IllegalArgumentException e) {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null) {
            List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(hoaDon.getId());
            if(listhdct.size() == 0){
                return "redirect:/admin/hoa-don/detail/" + tempIdHD;
            }
            hoaDon.setTrangThai(5);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("5");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        } else {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        alertInfo.alert("successTaiQuay","Đơn hàng đã được giao");
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    @GetMapping("updatehddg/{id}")
    public String updatehddg(
            @PathVariable(value = "id", required = false) String idhd,
            @RequestParam(value = "value", required = false) String ghiChu
    ) {
        try {
            UUID uuid = UUID.fromString(idhd);
        } catch (IllegalArgumentException e) {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(idhd);
        if (ghiChu == null || ghiChu.equals("null")) {
            ghiChu = " ";
        }
        if (ghiChu.length() > 20){
            alertInfo.alert("errTaiQuay","Tối đa 20 kí tự");
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        if (hoaDon != null) {
            hoaDon.setTrangThai(1);
            if (hoaDon.getLoaiHoaDon() == 2 || hoaDon.getLoaiHoaDon() == 3) {
                hinhThucThanhToan.setSoTien(hoaDon.getTongTien());
                hoaDon.setGhiChu(ghiChu);
                htttService.savehttt(hinhThucThanhToan);
            }
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("1");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        } else {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        alertInfo.alert("successTaiQuay","Đơn hàng đã hoàn thành");
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    public String handleHuyHd(String idhd, String liDoHuy) {
        try {
            UUID uuid = UUID.fromString(idhd);
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        if (liDoHuy.length() > 20){
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null) {
            hoaDon.setTrangThai(6);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("6");
            hoaDon.setGhiChu(liDoHuy);
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        } else {
            return "redirect:/admin/hoa-don/detail/" + tempIdHD;
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }
    @GetMapping("huyhdcxn/{id}")
    public String huyhdcxn(@PathVariable(value = "id", required = false) String idhd,
                           @RequestParam(value = "value", required = false) String liDoHuy
    ) {
        return handleHuyHd(idhd, liDoHuy);
    }

    @GetMapping("huyhdcg/{id}")
    public String huyhdcg(@PathVariable(value = "id", required = false) String idhd,
                          @RequestParam(value = "value", required = false) String liDoHuy

    ) {
        return handleHuyHd(idhd, liDoHuy);
    }
}