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
            "CASE WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h.createdDate) " +
            "     WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h.createdDate) " +
            "     WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h.createdDate) END AS datePart, " +
            "SUM(h.tongTien) AS tongDoanhThu, " +
            "SUM(CASE WHEN :unit = 'DAY' THEN h2.tongTien ELSE 0 END) AS tongTienCungKyTruoc " +
            "FROM HoaDon h " +
            "LEFT JOIN HoaDon h2 ON " +
            "   (CASE WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h2.createdDate) " +
            "         WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h2.createdDate) " +
            "         WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h2.createdDate) END) = " +
            "   (CASE WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM DATEADD(DAY, -1, h.createdDate)) " +
            "         WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM DATEADD(MONTH, -1, h.createdDate)) " +
            "         WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM DATEADD(YEAR, -1, h.createdDate)) END) " +
            "WHERE h.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY " +
            "CASE WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h.createdDate) " +
            "     WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h.createdDate) " +
            "     WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h.createdDate) END, h.createdDate")
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

    @Query("SELECT h.createdDate, COUNT(h) FROM HoaDon h " +
            "WHERE h.createdDate BETWEEN :startDate AND :endDate " +
            "GROUP BY h.createdDate")
    List<Object[]> countHoaDonByNgayTao(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
