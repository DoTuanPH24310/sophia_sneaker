package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonWebRepository extends JpaRepository<HoaDon, String> {
    @Query("SELECT " +
            "CASE " +
            "   WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h.createdDate) " +
            "   WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h.createdDate) " +
            "   WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h.createdDate) " +
            "   WHEN :unit = 'HOUR' THEN EXTRACT(HOUR FROM h.createdDate) " +
            "END AS datePart, " +
            "SUM(h.tongTien) AS tongDoanhThu, " +
            "SUM(CASE " +
            "         WHEN :unit = 'DAY' THEN h2.tongTien " +
            "         WHEN :unit = 'MONTH' THEN h2.tongTien " +
            "         WHEN :unit = 'YEAR' THEN h2.tongTien " +
            "         WHEN :unit = 'HOUR' THEN h2.tongTien " +
            "         ELSE 0 " +
            "     END) AS tongTienCungKyTruoc " +
            "FROM HoaDon h " +
            "LEFT JOIN HoaDon h2 ON " +
            "   (CASE " +
            "       WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h2.createdDate) " +
            "       WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h2.createdDate) " +
            "       WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h2.createdDate) " +
            "       WHEN :unit = 'HOUR' THEN EXTRACT(HOUR FROM h2.createdDate) " +
            "   END) = " +
            "   (CASE " +
            "       WHEN :unit = 'DAY' THEN EXTRACT(DAY FROM h.createdDate) " +
            "       WHEN :unit = 'MONTH' THEN EXTRACT(MONTH FROM h.createdDate) " +
            "       WHEN :unit = 'YEAR' THEN EXTRACT(YEAR FROM h.createdDate) " +
            "       WHEN :unit = 'HOUR' THEN EXTRACT(HOUR FROM h.createdDate) " +
            "   END) " +
            "WHERE h.createdDate BETWEEN :startDate AND :endDate " +
            "   AND h2.createdDate BETWEEN :startDate_minus_one_year AND :endDate_minus_one_year " +
            "GROUP BY datePart")
    List<Object[]> getDoanhThuTheoThang(@Param("startDate") LocalDateTime startDate,
                                        @Param("endDate") LocalDateTime endDate,
                                        @Param("unit") String unit,
                                        @Param("endDate_minus_one_year") LocalDateTime endDateMinusOneYear,
                                        @Param("startDate_minus_one_year") LocalDateTime startDateMinusOneYear);

    default String determineTimeUnit(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.getYear() != endDate.getYear()) {
            return "YEAR";
        } else if (startDate.getMonth() != endDate.getMonth()) {
            return "MONTH";
        } else if (startDate.getDayOfMonth() != endDate.getDayOfMonth()) {
            return "DAY";
        } else if (startDate.getHour() != endDate.getHour()) {
            return "HOUR";
        } else {
            return "SAME DATE";
        }
    }

    HoaDon findByMaHoaDOn(String hoadon);

}
