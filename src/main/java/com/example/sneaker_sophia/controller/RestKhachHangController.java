package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.service.TaiKhoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/khachHang")
public class RestKhachHangController {
    @Autowired
    private TaiKhoanService taiKhoanService;

    @GetMapping("/findAll")
    public ResponseEntity<?> findAllKH(){
        return ResponseEntity.ok(taiKhoanService.findAllKhachHang());
    }

    @GetMapping("/findByText")
    public ResponseEntity<?> findAllKH(@RequestParam("value") String value){
        System.out.println(value);
        System.out.println(taiKhoanService.findByText(value).size());
        return ResponseEntity.ok(taiKhoanService.findByText(value));
    }

    @GetMapping("/findListDiaChi")
    public ResponseEntity<?> findListDiaChi(@RequestParam("value") String idKh){
        return ResponseEntity.ok(taiKhoanService.findListTKById(idKh));
    }
}
