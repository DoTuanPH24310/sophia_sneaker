package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, UUID> {
    GioHang findByTaiKhoan(TaiKhoan taiKhoan);
}
