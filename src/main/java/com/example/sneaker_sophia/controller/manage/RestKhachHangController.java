package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.service.DiaChiService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khachHang")
public class RestKhachHangController {
    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @GetMapping("/findListDiaChi")
    public ResponseEntity<?> findListDiaChi(@RequestParam("value") String idKh){
        return ResponseEntity.ok(diaChiService.getAllDCByIdkh(idKh));
    }
}
