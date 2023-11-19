package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.service.HTTTService;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/hien-thi")
    public String hienthi(
            Model model
    ) {
        List<HoaDon> getAllHDC = hoaDonService.getAllHDC();
        List<HoaDon> getAllHDHT = hoaDonService.getAllHDHT();

        model.addAttribute("listhdc", getAllHDC);
        model.addAttribute("listhdht", getAllHDHT);
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
}
