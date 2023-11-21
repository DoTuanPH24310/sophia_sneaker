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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/admin/hoa-don")
public class QLHDController {

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
        return "admin/hoadon/detailhd";
    }

    @PostMapping("updatehdcxn")
    public String updatehdcxn(
            @RequestParam("idhd") List<String> listhd
    ){
        for (String idhd : listhd){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(4);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("chờ giao");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehdcg")
    public String updatehdcg(
            @RequestParam("idhd") List<String> listhdcg
    ){
        for (String idhd : listhdcg){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(5);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("đang giao");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }

    @PostMapping("updatehddg")
    public String updatehddg(
            @RequestParam("idhd") List<String> listhddg
    ){
        for (String idhd : listhddg){
            HoaDon hoaDon = hoaDonService.getHoaDonById(idhd);
            hoaDon.setTrangThai(1);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("hoàn thành");
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
            lichSuHoaDon.setPhuongThuc("chờ giao");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
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
            lichSuHoaDon.setPhuongThuc("đang giao");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
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
            lichSuHoaDon.setPhuongThuc("hoàn thành");
            lshdService.savelshd(lichSuHoaDon);
            hoaDonService.savehd(hoaDon);
        }
        return "redirect:/admin/hoa-don/hien-thi";
    }
}
