package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import com.example.sneaker_sophia.repository.DiaChiTamChu;
import com.example.sneaker_sophia.repository.LoginRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DiaChiCheckoutService {
    @Autowired
    private DiaChiTamChu diaChiTamChu;
    @Autowired
    private LoginRepository loginRepository;

    public DiaChi getDiaChiOfLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName(); // Lấy email người dùng đã đăng nhập
            TaiKhoan taiKhoan = this.loginRepository.getTaiKhoanByEmail(email);

            if (taiKhoan != null && taiKhoan.getDiaChiList() != null && !taiKhoan.getDiaChiList().isEmpty()) {
                return taiKhoan.getDiaChiList().get(0);
            }
        }
        return null;
    }

//    public void themDiaChiVaoTaiKhoan(DiaChi diaChi, TaiKhoan taiKhoan) {
//        diaChi.setTaiKhoan(taiKhoan);
//        taiKhoan.getDiaChiList().add(diaChi);
//        diaChiTamChu.save(diaChi);
//    }
}