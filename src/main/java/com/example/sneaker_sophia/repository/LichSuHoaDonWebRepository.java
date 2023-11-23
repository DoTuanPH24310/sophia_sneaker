package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LichSuHoaDonWebRepository extends JpaRepository<LichSuHoaDon, String> {
}
