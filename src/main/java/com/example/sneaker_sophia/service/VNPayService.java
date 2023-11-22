package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.VNPay;
import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.repository.VNPayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class VNPayService {
    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;
    @Autowired
    private VNPayRepository payRepository;

    public void saveVNPayInformation(double tienChuyenKhoan, String nganHang, String maGiaoDich, LocalDate ngayTao, String nguoiTao, Integer trangThai, String hoaDonId) {
        VNPay vnpay = new VNPay();
        vnpay.setTienChuyenKhoan(tienChuyenKhoan);
        vnpay.setNganHang(nganHang);
        vnpay.setMaGiaoDich(maGiaoDich);
        vnpay.setNgayTao(ngayTao);
        vnpay.setNguoiTao(nguoiTao);
        vnpay.setTrangThai(trangThai);

        // Link VNPay to HoaDon
        HoaDon hoaDon = hoaDonWebRepository.findById(hoaDonId).orElse(null);
        if (hoaDon != null) {
            vnpay.setHoaDon(hoaDon);
//            hoaDon.getVnpays().add(vnpay);
            hoaDonWebRepository.save(hoaDon);
        }

        payRepository.save(vnpay);
    }
}
