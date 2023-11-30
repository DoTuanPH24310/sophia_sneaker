package com.example.sneaker_sophia.controller;
import com.example.sneaker_sophia.dto.DTO_API_HoaDon;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        if (hd.getTrangThai() == 2){
            hd.setLoaiHoaDon(1);
        }
        hoaDonService.savehd(hd);
    }

    @GetMapping("/deleteSession/{name}")
    public void delete(@PathVariable("name") String name){
        session.removeAttribute(name);
    }
    //    20/11

    @PostMapping("/multipleFindHoaDon")
    public ResponseEntity<?> multipleFind(@RequestBody DTO_API_HoaDon idGiay) {
        return ResponseEntity.ok(hoaDonService.findHoaDonByMultipleParamsAPI(idGiay));
    }
}