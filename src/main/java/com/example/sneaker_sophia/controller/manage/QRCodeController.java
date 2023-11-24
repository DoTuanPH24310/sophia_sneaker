package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.QRCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class QRCodeController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @GetMapping("/qrcode")
    public ResponseEntity<String> generateAllQRCodes() {
        try {
            List<ChiTietGiay> chiTietGiayList = chiTietGiayRepository.findAll();

            if (chiTietGiayList.isEmpty()) {
                return ResponseEntity.ok("No ChiTietGiay found to generate QR Codes.");
            }

            // Thư mục để lưu tất cả ảnh QR (thay đổi theo nhu cầu của bạn)
            String folderPath = "C:/intern/Newwave";

            // Gọi service để tạo và lưu tất cả ảnh QR
            qrCodeService.generateAndSaveQRCodeImages(chiTietGiayList, folderPath);

            return ResponseEntity.ok("QR Codes generated and saved in folder: " + folderPath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating QR Codes: " + e.getMessage());
        }
    }
}
