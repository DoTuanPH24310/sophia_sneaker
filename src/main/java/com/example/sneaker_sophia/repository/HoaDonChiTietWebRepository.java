package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietWebRepository extends JpaRepository<HoaDonChiTiet, String> {

    @Query("SELECT h.chiTietGiay.ten AS tenChiTietGiay, SUM(h.soLuong) AS tongSoLuong " +
            "FROM HoaDonChiTiet h " +
            "WHERE YEAR(h.createdDate) = :nam " +
            "GROUP BY h.chiTietGiay " +
            "ORDER BY tongSoLuong DESC")
    List<Object[]> findTop10BestSellingProductsByNam(@Param("nam") int nam);

}
