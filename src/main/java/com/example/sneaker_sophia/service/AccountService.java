package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.AccountRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private AccountRepository accountRepository;

    public List<DiaChi> findByTaiKhoan_Email(String tenTaiKhoan) {
        return accountRepository.findByTaiKhoan_Email(tenTaiKhoan);
    }

    public void updateTaiKhoan(TaiKhoanDTO updatedTaiKhoanDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        TaiKhoan taiKhoan = loginRepository.findByEmail(email);

        if (taiKhoan != null) {
            updatedTaiKhoanDTO.loadFormTaiKhoan(taiKhoan);
            taiKhoan.setTen(updatedTaiKhoanDTO.getTen());
            taiKhoan.setGioiTinh(updatedTaiKhoanDTO.getGioiTinh());
            taiKhoan.setSdt(updatedTaiKhoanDTO.getSdt());
            taiKhoan.setNgaySinh(updatedTaiKhoanDTO.getNgaySinh());
            this.loginRepository.save(taiKhoan);
        } else {
            // Xử lý khi không tìm thấy tài khoản, ví dụ: throw exception hoặc trả về thông báo lỗi
        }
    }

}
