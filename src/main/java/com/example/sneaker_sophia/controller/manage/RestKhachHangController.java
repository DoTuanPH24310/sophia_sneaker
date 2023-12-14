package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.service.DiaChiService;
import com.example.sneaker_sophia.service.TaiKhoanService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khachHang")
public class RestKhachHangController {
    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/findListDiaChi")
    public ResponseEntity<?> findListDiaChi(@RequestParam("value") String idKh){
        return ResponseEntity.ok(diaChiService.getAllDCByIdkh(idKh));

    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllKH(){
        return ResponseEntity.ok(taiKhoanService.getAllTaiKhoanByTrangThai());
    }

    @GetMapping("/findByText")
    public ResponseEntity<?> findAllKH(@RequestParam("value") String value){
        return ResponseEntity.ok(taiKhoanService.findByText(value));
    }

    @GetMapping("/getEmail")
    public ResponseEntity<?> getEmail(
            @RequestParam("email") String email
    ){

        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByEmail(email);
        System.out.println(taiKhoan);
        return ResponseEntity.ok(taiKhoanService.getTaiKhoanByEmail(email));
    }

}
