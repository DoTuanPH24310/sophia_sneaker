package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonChiTietWebRepository extends JpaRepository<HoaDonChiTiet, String> {

    @Query("SELECT " +
            "CASE " +
            "   WHEN :unit = 'DAY' THEN DAY(h.createdDate) " +
            "   WHEN :unit = 'MONTH' THEN MONTH(h.createdDate) " +
            "   WHEN :unit = 'YEAR' THEN YEAR(h.createdDate) END AS datePart, " +
            "h.chiTietGiay.hang.ten || ' ' || h.chiTietGiay.giay.ten || ' ' || h.chiTietGiay.ten || ' ' || h.chiTietGiay.mauSac.ten|| ' ' || h.chiTietGiay.loaiGiay.ten AS tenGiay, " +
            "SUM(h.soLuong) AS tongSoLuong " +
            "FROM HoaDonChiTiet h " +
            "WHERE h.createdDate BETWEEN :startDate AND :endDate AND (h.chiTietGiay.trangThai = 1 OR h.chiTietGiay.trangThai = 0) " +
            "GROUP BY datePart, h.chiTietGiay.hang.ten, h.chiTietGiay.ten, h.chiTietGiay.giay.ten, h.chiTietGiay.mauSac.ten,h.chiTietGiay.loaiGiay.ten,h.chiTietGiay.kichCo.ten,h.chiTietGiay.id " +
            "ORDER BY tongSoLuong DESC")
    List<Object[]> getTop10ChiTietSanPham(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("unit") String unit);

}
