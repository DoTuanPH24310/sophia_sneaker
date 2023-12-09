package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface LichSuHoaDonWebRepository extends JpaRepository<LichSuHoaDon, String> {
    @Query("SELECT COUNT(ls) FROM LichSuHoaDon ls " +
            "WHERE ls.hoaDon.trangThai = 1 " +
            "AND ls.phuongThuc = '3' " +
            "AND ls.createdDate BETWEEN :startDate AND :endDate")
    int countByHoaDonTrangThaiAndPhuongThuc(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(hd.tongTien), 0) FROM LichSuHoaDon ls " +
            "JOIN ls.hoaDon hd " +
            "WHERE ls.hoaDon.trangThai = 1 " +
            "AND ls.phuongThuc = '3' " +
            "AND ls.createdDate BETWEEN :startDate AND :endDate")
    Double sumTongTienByHoaDonTrangThaiAndPhuongThuc(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    LichSuHoaDon findByHoaDon(HoaDon hoaDon);
}
