package com.example.sneaker_sophia.controller.manage;


import com.example.sneaker_sophia.dto.DTO_API_CTG;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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



    @Resource(name = "anhRepository")
    AnhRepository anhRepository;

    @GetMapping("/allCTG")
    public ResponseEntity<?> getListCTG(HttpSession session) {
        List<ChiTietGiay> list = chiTietGiayService.getAll();
        for (ChiTietGiay chiTietGiay : list) {
            String avtctg = anhRepository.getAnhChinhByIdctg(chiTietGiay.getId());
            session.setAttribute("avtctsp", avtctg);
        }
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
    public ResponseEntity<?> multipleFind(@RequestBody DTO_API_CTG idGiay) {
        return ResponseEntity.ok(chiTietGiayService.findChiTietGiayByMultipleParamsAPI(idGiay));
    }


    @PostMapping("/soLuongTon")
    public ResponseEntity<?> getSoLuongTon(@RequestParam("value") String slt) {

        return ResponseEntity.ok(chiTietGiayService.findSoLuongTon(slt));
    }


    @GetMapping("getSLTBYQR/{qr}")
    public ResponseEntity<?> getSLT(@PathVariable("qr") String qr) {
        return ResponseEntity.ok(chiTietGiayService.findSoLuongTonByQrCode(qr));
    }

    @GetMapping("getSumKhuyenMai/{idCTG}")
    public ResponseEntity<?> getSumKhuyenMai(@PathVariable("idCTG") String id) {
        return ResponseEntity.ok(chiTietGiayService.tongKMByIdctg(UUID.fromString(id)));
    }
//Mới

    @GetMapping("/findhang")
    public ResponseEntity<?> getAllHang() {
        return ResponseEntity.ok(hangService.finAllTrangThai());
    }

    @GetMapping("/findgiay")
    public ResponseEntity<?> getAllGiay() {
        return ResponseEntity.ok(giayService.finAllTrangThai());
    }

    @GetMapping("/findde")
    public ResponseEntity<?> getAllDe() {
        return ResponseEntity.ok(deGiayService.finAllTrangThai());
    }

    @GetMapping("/findloaiGiay")
    public ResponseEntity<?> getAllLoaiGiay() {
        return ResponseEntity.ok(loaiGiayService.finAllTrangThai());
    }

    @GetMapping("/findsize")
    public ResponseEntity<?> getAllSize() {
        return ResponseEntity.ok(kichCoService.finAllTrangThai());
    }

    @GetMapping("/findmau")
    public ResponseEntity<?> getAllMau() {
        return ResponseEntity.ok(mauSacService.finAllTrangThai());
    }



//    Mới

    @PostMapping("/addgiay")
    public void addGiay(@RequestBody Giay giay) {
        giayService.save(giay);
    }



    @PostMapping("/addsize")
    public void addKichCo(@RequestBody KichCo kichCo) {
        kichCoService.save(kichCo);
    }


    @PostMapping("/addde")
    public void addDeGiay(@RequestBody DeGiay deGiay) {
        deGiayService.save(deGiay);
    }


    @PostMapping("/addhang")
    public void addHang(@RequestBody Hang giay) {
        hangService.save(giay);
    }



    @PostMapping("/addloaiGiay")
    public void addbtnloaiGiay(@RequestBody LoaiGiay loaiGiay) {
        loaiGiayService.save(loaiGiay);
    }


    @PostMapping("/addmau")
    public void addbtnMauSac(@RequestBody MauSac mau) {
        mauSacService.save(mau);
    }

}