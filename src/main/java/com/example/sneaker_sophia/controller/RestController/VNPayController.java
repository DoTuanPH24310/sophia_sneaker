package com.example.sneaker_sophia.controller.RestController;

import com.example.sneaker_sophia.dto.VNPayDTO;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.repository.HoaDonRepository;
import com.example.sneaker_sophia.repository.HoaDonWebRepository;
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

    @Autowired
    private HoaDonWebRepository hoaDonWebRepository; // Giả sử bạn có một repository cho HoaDon

    @PostMapping("/callback")
    public ResponseEntity<String> handleVnPayCallback(@RequestBody VNPayDTO callbackRequest) {
        // Xử lý thông báo xác nhận từ VNPAY
        // Trích xuất thông tin cần thiết như mã đơn hàng, mã giao dịch, số tiền thanh toán và trạng thái

        // Kết nối cơ sở dữ liệu và cập nhật số tiền đã thanh toán
        boolean success = updatePaymentAmount(callbackRequest.getMaGiaoDich(), callbackRequest.getTienChuyenKhoan());

        if (success) {
            return ResponseEntity.ok("Xác nhận thanh toán thành công");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật cơ sở dữ liệu");
        }
    }

    private boolean updatePaymentAmount(String orderId, double amount) {
        // Lấy hóa đơn từ cơ sở dữ liệu bằng mã đơn hàng
        HoaDon hoaDon = hoaDonWebRepository.findByMaHoaDOn(orderId);

        // Kiểm tra xem hóa đơn có tồn tại không
        if (hoaDon != null) {
            // Cập nhật số tiền đã thanh toán
            hoaDon.setTongTien(hoaDon.getTongTien() + amount);

            // Lưu lại vào cơ sở dữ liệu
            this.hoaDonWebRepository.save(hoaDon);

            return true;
        } else {
            return false;
        }
    }
}
