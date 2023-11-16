package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietWebRepository extends JpaRepository<HoaDonChiTiet, String> {
    @Query("SELECT h.chiTietGiay.ten AS tenChiTietGiay, h.soLuong AS soLuong " +
            "FROM HoaDonChiTiet h " +
            "WHERE YEAR(h.createdDate) = :nam")
    List<Object[]> findChiTietGiayAndSoLuongByNam(@Param("nam") int nam);
}
