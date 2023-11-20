package com.example.sneaker_sophia.controller.RestController;

import com.example.sneaker_sophia.dto.VNPayDTO;
import com.example.sneaker_sophia.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/vnpay/callback")
    public ResponseEntity<String> vnpayCallback(@RequestBody VNPayDTO vnPayDTO) {
        try {
            double tienChuyenKhoan = vnPayDTO.getTienChuyenKhoan();
            String nganHang = vnPayDTO.getNganHang();
            String maGiaoDich = vnPayDTO.getMaGiaoDich();
            LocalDate ngayTao = LocalDate.now();
            String nguoiTao = "VNPay";
            Integer trangThai = 1;
            String hoaDonId = vnPayDTO.getHoaDon().getId();

            vnPayService.saveVNPayInformation(tienChuyenKhoan, nganHang, maGiaoDich, ngayTao, nguoiTao, trangThai, hoaDonId);

            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }
}
