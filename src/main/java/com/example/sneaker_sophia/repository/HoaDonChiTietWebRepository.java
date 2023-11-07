package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonChiTietWebRepository extends JpaRepository<HoaDonChiTiet, String> {
}
