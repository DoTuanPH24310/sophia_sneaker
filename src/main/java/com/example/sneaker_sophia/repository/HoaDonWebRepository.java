package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonWebRepository extends JpaRepository<HoaDon, String> {
    @Query("SELECT MONTH(h.ngayNhan) AS thang, SUM(h.tongTien) AS tongDoanhThu "
            + "FROM HoaDon h "
            + "WHERE YEAR(h.ngayNhan) = :nam "
            + "GROUP BY MONTH(h.ngayNhan)")
    List<Object[]> getDoanhThuTheoThang(@Param("nam") int nam);

    HoaDon findByMaHoaDOn(String hoadon);
}
