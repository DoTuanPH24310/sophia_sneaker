package com.example.sneaker_sophia.controller;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/hoa-don")
public class RestControllerHoaDon {

    @Autowired
    private HoaDonService hoaDonService;


    @Autowired
    private HoaDonChiTietServive hoaDonChiTietServive;

    @Autowired
    private HttpSession session;


    @GetMapping("/updateGiaoHang/{idHD}")
    public void updateGH(@PathVariable("idHD") HoaDon hd) {
        hd.setLoaiHoaDon(2);
        hoaDonService.savehd(hd);
    }

    @GetMapping("/updateTaiQuay/{idHD}")
    public void updateTQ(@PathVariable("idHD") HoaDon hd) {
        hd.setLoaiHoaDon(1);
        hoaDonService.savehd(hd);
    }

    @GetMapping("/deleteSession/{name}")
    public void delete(@PathVariable("name") String name){
        session.removeAttribute(name);
    }


    @GetMapping("/getTongTien/{idHD}")
    public Double getTongTien(@PathVariable("idHD") String idHD){
        return hoaDonChiTietServive.tongTienHD(idHD);
    }
}