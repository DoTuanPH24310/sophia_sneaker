package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.TaiKhoan;
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
import java.util.Optional;

@Repository
public interface HoaDonWebRepository extends JpaRepository<HoaDon, String> {

    @Query(value = "SELECT " +
            "   CONCAT(DATEPART(MONTH, h.ngaySua), '/', DATEPART(YEAR, h.ngaySua)) AS ThangNam, " +
            "   SUM(h.tongTien) AS TongDoanhThu, " +
            "   (SELECT SUM(h1.tongTien) " +
            "    FROM HoaDon h1 " +
            "    WHERE DATEPART(YEAR, h1.ngaySua) = DATEPART(YEAR, :startDate) - 1 " +
            "      AND DATEPART(MONTH, h1.ngaySua) = DATEPART(MONTH, h.ngaySua) " +
            "      AND h1.trangThai = 1) AS TongDoanhThuNamTruoc " +
            "FROM " +
            "   HoaDon h " +
            "WHERE " +
            "   DATEPART(YEAR, h.ngaySua) = DATEPART(YEAR, :startDate) " +
            "   AND h.trangThai = 1 " +
            "GROUP BY " +
            "   DATEPART(YEAR, h.ngaySua), DATEPART(MONTH, h.ngaySua)", nativeQuery = true)
    List<Object[]> getStatisticsByMonth(
            @Param("startDate") LocalDateTime startDate);


    @Query(value = "SELECT " +
            "   CONVERT(VARCHAR, h.ngaySua, 23) AS date, " +
            "   SUM(h.tongTien) AS totalAmountCurrentMonth, " +
            "   COALESCE(LAG(SUM(h.tongTien)) OVER (ORDER BY CONVERT(VARCHAR, h.ngaySua, 23)), 0) AS totalAmountPreviousMonth " +
            "FROM " +
            "   HoaDon h " +
            "WHERE " +
            "   DATEPART(YEAR, h.ngaySua) = DATEPART(YEAR, :startDate) " +
            "   AND DATEPART(MONTH, h.ngaySua) = DATEPART(MONTH, :startDate) " +
            "   AND h.trangThai = 1 " +
            "GROUP BY " +
            "   CONVERT(VARCHAR, h.ngaySua, 23)", nativeQuery = true)
    List<Object[]> getStatisticsByDay(@Param("startDate") LocalDateTime startDate);

    @Query(value = "WITH DataForCurrentDay AS (" +
            "   SELECT " +
            "      DATEPART(HOUR, h.ngaySua) AS hour, " +
            "      SUM(h.tongTien) AS tongTien " +
            "   FROM " +
            "      HoaDon h " +
            "   WHERE " +
            "      h.trangThai = 1 " +
            "      AND CONVERT(DATE, h.ngaySua) = CONVERT(DATE, :startDate) " +
            "   GROUP BY " +
            "      DATEPART(HOUR, h.ngaySua)" +
            "), " +
            "DataForPreviousDay AS (" +
            "   SELECT " +
            "      DATEPART(HOUR, h.ngaySua) AS hour, " +
            "      SUM(h.tongTien) AS tongTien " +
            "   FROM " +
            "      HoaDon h " +
            "   WHERE " +
            "      h.trangThai = 1 " +
            "      AND CONVERT(DATE, h.ngaySua) = DATEADD(DAY, -1, CONVERT(DATE, :startDate))" +
            "   GROUP BY " +
            "      DATEPART(HOUR, h.ngaySua)" +
            "), " +
            "AllHours AS (" +
            "   SELECT " +
            "      number AS hour " +
            "   FROM " +
            "      master.dbo.spt_values " +
            "   WHERE " +
            "      type = 'P' " +
            "      AND number BETWEEN 0 AND 23" +
            ") " +
            "SELECT " +
            "   ah.hour AS hour, " +
            "   COALESCE(SUM(d1.tongTien), 0) AS totalAmountCurrentDay, " +
            "   COALESCE(SUM(d2.tongTien), 0) AS totalAmountPreviousDay " +
            "FROM AllHours ah " +
            "LEFT JOIN DataForCurrentDay d1 ON ah.hour = d1.hour " +
            "LEFT JOIN DataForPreviousDay d2 ON ah.hour = d2.hour " +
            "GROUP BY " +
            "   ah.hour " +
            "ORDER BY " +
            "   ah.hour", nativeQuery = true)
    List<Object[]> getStatisticsByHour(@Param("startDate") LocalDateTime startDate);


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

    @Query(value = "SELECT " +
            "   DATEPART(YEAR, h.ngaySua) AS Nam, " +
            "   SUM(h.tongTien) AS TongDoanhThu " +
            "FROM " +
            "   HoaDon h " +
            "WHERE " +
            "   h.trangThai = 1 " +
            "GROUP BY " +
            "   DATEPART(YEAR, h.ngaySua)", nativeQuery = true)
    List<Object[]> getSumTongTienByYear();

    HoaDon findByMaHoaDOn(String hoadon);


    List<HoaDon> findByTaiKhoanAndLoaiHoaDonEqualsOrderByCreatedDateDesc(TaiKhoan tk,Integer lhd);




    //tinh % tang giam theo ngay
    @Query("SELECT COALESCE(SUM(hd.tongTien), 0) FROM HoaDon hd WHERE CAST(hd.updatedDate AS DATE) = CURRENT_DATE And hd.trangThai=1")
    Double sumTongTienByNgayHienTai();

    @Query("SELECT COALESCE(SUM(hd.tongTien), 0) FROM HoaDon hd WHERE hd.updatedDate >= :ngayBatDau AND hd.updatedDate <= :ngayKetThuc And hd.trangThai=1")
    Double sumTongTienByNgayHomQua(@Param("ngayBatDau") LocalDateTime ngayBatDau, @Param("ngayKetThuc") LocalDateTime ngayKetThuc);

    // end tinh % tang giam theo ngay
    //tinh % tang giam theo Thang
    @Query("SELECT COALESCE(SUM(hd.tongTien), 0) " +
            "FROM HoaDon hd " +
            "WHERE FUNCTION('MONTH', hd.updatedDate) = FUNCTION('MONTH', CURRENT_DATE) " +
            "AND FUNCTION('YEAR', hd.updatedDate) = FUNCTION('YEAR', CURRENT_DATE) And hd.trangThai=1")
    Double sumTongTienByThangHienTai();

    @Query("SELECT COALESCE(SUM(hd.tongTien), 0) FROM HoaDon hd WHERE hd.updatedDate >= :ngayBatDau AND hd.updatedDate <= :ngayKetThuc And hd.trangThai=1")
    Double sumTongTienTuDauThangDenNgayHienTaiThangTruoc(
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc
    );
    // end tinh % tang giam theo Thang
}
