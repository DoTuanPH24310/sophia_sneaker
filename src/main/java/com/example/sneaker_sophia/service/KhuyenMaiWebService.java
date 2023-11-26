package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.CTG_KhuyenMai;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Voucher;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class KhuyenMaiWebService {

    public void tinhGiaSauKhuyenMai(ChiTietGiay chiTietGiay, HttpSession session) {
        List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();
        Double giaBan = chiTietGiay.getGia();
        Double giaKhuyenMai = giaBan;
        Double giamGia = 0.0;
        if (listCTG_KM != null && !listCTG_KM.isEmpty()) {
            for (CTG_KhuyenMai ctg : listCTG_KM) {
                Voucher voucher = ctg.getId().getVoucher();
                if (voucher.getTrangThai() == 1) {
                    Integer phanTramGiam = voucher.getPhanTramGiam();
                    if (phanTramGiam != null) {
                        giamGia = (phanTramGiam * giaBan) / 100.0;
                        giaKhuyenMai -= giamGia;
                    }
                }
            }
        }
        session.setAttribute("giamGia", giamGia);
        session.setAttribute("giaCu_" + chiTietGiay.getId(), giaBan);
        session.setAttribute("giaMoi_" + chiTietGiay.getId(), giaKhuyenMai);

    }


}
