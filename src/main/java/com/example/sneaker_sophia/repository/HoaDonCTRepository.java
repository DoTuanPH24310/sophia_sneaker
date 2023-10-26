package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.dto.IdHoaDonCT;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hoaDonCTRepository")
public interface HoaDonCTRepository extends JpaRepository<HoaDonChiTiet, Object> {
    @Query(value = "select * from HoaDonChiTiet where IdHoaDon = ?1", nativeQuery = true)
    List<HoaDonChiTiet> getHDCTByIdHD(String idhd);
}
