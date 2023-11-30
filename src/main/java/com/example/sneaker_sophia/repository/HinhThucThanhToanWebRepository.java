package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhThucThanhToanWebRepository extends JpaRepository<HinhThucThanhToan, String> {
    HinhThucThanhToan findByHoaDon(HoaDon hoaDon);
}
