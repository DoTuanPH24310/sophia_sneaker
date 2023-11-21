package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.entity.LichSuHoaDon;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.service.HTTTService;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import com.example.sneaker_sophia.service.LSHDService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
            @PathVariable("id") String idhd,
            Model model
    ) {
        tempIdHD = idhd;
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(idhd);
        Map<UUID, String> avtctgMap = new HashMap<>();
        for (HoaDonChiTiet hdct : listhdct) {
            UUID idctg = hdct.getChiTietGiay().getId();
            String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
            avtctgMap.put(idctg, avtctg);
            model.addAttribute("avtctgMap", avtctgMap);
        }
        HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(idhd);
        if(hinhThucThanhToan != null){
            model.addAttribute("httt", hinhThucThanhToan);
        }
        model.addAttribute("displayTable", hinhThucThanhToan != null);
        model.addAttribute("listhdct", listhdct);
        model.addAttribute("hoaDon", hoaDon);
        System.out.println("test: ");
        System.out.println(lshdService.getLSHDBYIdhd(idhd).size());
        session.setAttribute("lichSuHoaDon",lshdService.getLSHDBYIdhd(idhd));
        return "admin/hoadon/detailhd";
    }

    @PostMapping("updatehdcxn")
    public String updatehdcxn(
            @RequestParam(value = "idhd", required = false) List<String> listhd
    ){
        if(listhd == null){
            return "redirect:/admin/hoa-don/hien-thi";
        }
        for (String idhd : listhd){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(4);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("4");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehdcg")
    public String updatehdcg(
            @RequestParam(value = "idhd", required = false) List<String> listhdcg
    ){
        if(listhdcg == null){
            return "redirect:/admin/hoa-don/hien-thi";
        }
        for (String idhd : listhdcg){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(5);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("5");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehddg")
    public String updatehddg(
            @RequestParam(value = "idhd", required = false) List<String> listhddg
    ){
        if(listhddg == null){
            return "redirect:/admin/hoa-don/hien-thi";
        }
        for (String idhd : listhddg){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(1);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("1");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @GetMapping("updatehdcxn/{id}")
    public String updatehdcxnd(
        @PathVariable("id") String idhd
    ){
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null){
            hoaDon.setTrangThai(4);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("4");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    @GetMapping("updatehdcg/{id}")
    public String updatehdcg(
            @PathVariable("id") String idhd
    ){
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null){
            hoaDon.setTrangThai(5);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("5");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;

    }

    @GetMapping("updatehddg/{id}")
    public String updatehddg(
            @PathVariable("id") String idhd
    ){
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null){
            hoaDon.setTrangThai(1);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("1");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    public String handleHuyHd(String idhd) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
        if (hoaDon != null) {
            hoaDon.setTrangThai(6);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("6");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/detail/" + tempIdHD;
    }

    @GetMapping("huyhdcxn/{id}")
    public String huyhdcxn(@PathVariable("id") String idhd) {
        return handleHuyHd(idhd);
    }

    @GetMapping("huyhdcg/{id}")
    public String huyhdcg(@PathVariable("id") String idhd) {
        return handleHuyHd(idhd);
    }
}
