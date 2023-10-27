package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.dto.IdHoaDonCT;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("hoaDonCTRepository")
public interface HoaDonCTRepository extends JpaRepository<HoaDonChiTiet, Object> {
    @Query(value = "select * from HoaDonChiTiet where IdHoaDon = ?1", nativeQuery = true)
    List<HoaDonChiTiet> getHDCTByIdHD(String idhd);

    @Query(value = "delete from HoaDonChiTiet where IdChiTietGiay = ?1", nativeQuery = true)
    HoaDonChiTiet deleteByIdCTSP(UUID idctsp);
}
