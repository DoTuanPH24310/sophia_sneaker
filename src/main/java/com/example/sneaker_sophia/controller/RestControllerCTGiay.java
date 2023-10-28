package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.dto.DTO_API_CTG;
import com.example.sneaker_sophia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/chi-tiet-giay")
public class RestControllerCTGiay {
    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @Autowired
    private HangService hangService;

    @Autowired
    private DeGiayService deGiayService;

    @Autowired
    private GiayService giayService;


    @Autowired
    private KichCoService kichCoService;

    @Autowired
    private MauSacService mauSacService;

    @Autowired
    private LoaiGiayService loaiGiayService;


    @GetMapping("/allCTG")
    public ResponseEntity<?> getListCTG() {
        return ResponseEntity.ok(chiTietGiayService.findAllByTrangThaiEquals(0));
    }

    @GetMapping("/hang")
    public ResponseEntity<?> getListHang() {
        return ResponseEntity.ok(hangService.findByTrangThaiEquals(0));
    }

    @GetMapping("/giay")
    public ResponseEntity<?> getListGiay() {
        return ResponseEntity.ok(giayService.findAllByTrangThaiEquals(0));
    }

    @GetMapping("/de")
    public ResponseEntity<?> getDe() {
        return ResponseEntity.ok(deGiayService.findByTrangThaiEquals(0));
    }

    @GetMapping("/loaiGiay")
    public ResponseEntity<?> getLoaiGiay() {
        return ResponseEntity.ok(loaiGiayService.findByTrangThaiEquals(0));
    }

    @GetMapping("/size")
    public ResponseEntity<?> getSize() {
        return ResponseEntity.ok(kichCoService.findByTrangThaiEquals(0));
    }

    @GetMapping("/mau")
    public ResponseEntity<?> getMau() {
        return ResponseEntity.ok(mauSacService.findByTrangThaiEquals(0));
    }

    @PostMapping("/multipleFind")
    public ResponseEntity<?> multipleFind(@RequestBody DTO_API_CTG idGiay){
        System.out.println(idGiay +"test");

        return ResponseEntity.ok(chiTietGiayService.findChiTietGiayByMultipleParamsAPI(idGiay));
    }
}