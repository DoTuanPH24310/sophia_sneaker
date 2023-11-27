package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonWebRepository extends JpaRepository<HoaDon, String> {
    @Query("SELECT " +
            "CASE WHEN :unit = 'DAY' THEN DAY(h.createdDate) " +
            "     WHEN :unit = 'MONTH' THEN MONTH(h.createdDate) " +
            "     WHEN :unit = 'YEAR' THEN YEAR(h.createdDate) END AS datePart, " +
            "SUM(h.tongTien) AS tongDoanhThu " +
            "FROM HoaDon h " +
            "WHERE h.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY " +
            "CASE WHEN :unit = 'DAY' THEN DAY(h.createdDate) END, " +
            "CASE WHEN :unit = 'MONTH' THEN MONTH(h.createdDate) END, " +
            "CASE WHEN :unit = 'YEAR' THEN YEAR(h.createdDate) END, h.createdDate")
    List<Object[]> getDoanhThuTheoThang(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("unit") String unit);


    default String determineTimeUnit(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.getYear() != endDate.getYear()) {
            return "YEAR";
        } else if (startDate.getMonth() != endDate.getMonth()) {
            return "MONTH";
        } else {
            return "DAY";
        }
    }

    HoaDon findByMaHoaDOn(String hoadon);
}
