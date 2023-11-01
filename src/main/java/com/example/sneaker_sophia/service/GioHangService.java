package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GioHangService {

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;

    public GioHang getCartByEmail(String email) {
        TaiKhoan taiKhoan = loginRepository.findByEmail(email);
        return gioHangRepository.findByTaiKhoan(taiKhoan);
    }

    public List<GioHangChiTiet> getCartItems(UUID gioHangId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        return gioHangChiTietRepository.findById_GioHang(gioHang);
    }

}
