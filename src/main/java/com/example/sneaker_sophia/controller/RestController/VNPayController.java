package com.example.sneaker_sophia.controller.RestController;

import com.example.sneaker_sophia.dto.VNPayDTO;
import com.example.sneaker_sophia.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/vnpay")
public class VNPayController {
    @Autowired
    private VNPayService vnPayService;

    @PostMapping("/callback")
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

    @GetMapping("/thanhtoan")
    public String xuLyKetQuaThanhToan(@RequestParam Map<String, String> vnpResponse, Model model) {
        // Kiểm tra và xác thực chữ ký từ VNPAY
        boolean ketQuaThanhToan = kiemTraChuKyVNPAY(vnpResponse);

        // Lấy thông tin chi tiết từ kết quả thanh toán
        String maDonHang = vnpResponse.get("vnp_TxnRef");
        String trangThaiThanhToan = vnpResponse.get("vnp_ResponseCode");

        // Kiểm tra xem thanh toán có thành công hay không
        if (ketQuaThanhToan && "00".equals(trangThaiThanhToan)) {
            // Xử lý khi thanh toán thành công
            // Cập nhật trạng thái đơn hàng trong hệ thống của bạn
            model.addAttribute("message", "Thanh toán thành công");
            return "thanh-cong";
        } else {
            // Xử lý khi thanh toán thất bại
            // Cập nhật trạng thái đơn hàng trong hệ thống của bạn nếu cần
            model.addAttribute("message", "Thanh toán thất bại");
            return "that-bai";
        }
    }

    // Hàm kiểm tra chữ ký từ VNPAY
    private boolean kiemTraChuKyVNPAY(Map<String, String> vnpResponse) {
        // Thực hiện kiểm tra chữ ký dựa trên thông tin nhận được từ VNPAY
        // Chi tiết kiểm tra chữ ký có thể được thực hiện theo hướng dẫn của VNPAY
        // Trả về true nếu chữ ký hợp lệ, ngược lại trả về false
        // (Chú ý: Bạn cần tham gia vào hướng dẫn của VNPAY để hiểu cách xác thực chữ ký đúng cách)
        return true;
    }
}
