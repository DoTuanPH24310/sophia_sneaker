package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("hoaDonRepository")
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    @Query(value = "  Select COUNT(MaHoaDon) from HoaDon", nativeQuery = true)
    Integer soHD();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 2 and hd.loaiHoaDon in (1,2) order by hd.maHoaDOn")
    List<HoaDon> getHoaDonByTrangThai();


    @Query(value = "select count(maHoaDOn) from HoaDon where trangThai = 2 and loaiHoaDon in (1,2)", nativeQuery = true)
    Integer countHoaDonByTrangThai();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 1 order by hd.createdDate desc ")
    List<HoaDon> getAllHDHT();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 2 order by hd.createdDate desc ")
    List<HoaDon> getAllHDC();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 3 order by hd.createdDate desc ")
    List<HoaDon> getAllHDCXN();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 4 order by hd.createdDate desc ")
    List<HoaDon> getAllHDChoGiao();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 5 order by hd.createdDate desc ")
    List<HoaDon> getAllHDDangGiao();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 6 order by hd.createdDate desc ")
    List<HoaDon> getAllHDHuy();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 7 order by hd.createdDate desc ")
    List<HoaDon> getAllHDHoan();

    @Query(value = "select count(hd) from HoaDon hd where hd.trangThai = 5")
    Integer soHDDG();

    @Query(value = "select count(hd) from HoaDon hd where hd.trangThai = 3")
    Integer soHDCXN();

    @Query(value = "select count(hd) from HoaDon hd where hd.trangThai = 4")
    Integer soHDCG();

    @Query("SELECT c FROM HoaDon c WHERE" +
            "(:ngayBatDau IS NULL OR c.createdDate >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR c.createdDate <= :ngayKetThuc) AND " +
            "(:loaiHoaDon IS NULL OR c.loaiHoaDon = :loaiHoaDon) AND " +
            "((:textSearch IS NULL OR c.maHoaDOn like :textSearch) or" +
            "(:textSearch IS NULL OR c.soDienThoai like :textSearch) or" +
            "(:textSearch IS NULL OR c.taiKhoan.ten like :textSearch) or " +
            "(:textSearch IS NULL OR c.tenKhachHang like :textSearch)) AND" +
            "(c.trangThai = :trangThai)" +
            "order by c.createdDate desc ")
    List<HoaDon> findHoaDonByMultipleParamsAPIDate(
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            @Param("trangThai") Integer trangThai,
            @Param("loaiHoaDon") Integer loaiHoaDon,
            @Param("textSearch") String textSearch);

    @Query("SELECT c FROM HoaDon c WHERE" +
            "(:ngayBatDau IS NULL OR c.createdDate >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR c.createdDate <= :ngayKetThuc) AND " +
            "(:loaiHoaDon IS NULL OR c.loaiHoaDon = :loaiHoaDon) AND " +
            "((:textSearch IS NULL OR c.maHoaDOn like :textSearch) or" +
            "(:textSearch IS NULL OR c.soDienThoai like :textSearch) or" +
            "(:textSearch IS NULL OR c.taiKhoan.ten like :textSearch) or " +
            "(:textSearch IS NULL OR c.tenKhachHang like :textSearch)) AND" +
            "(c.trangThai = :trangThai)" +
            "order by c.tongTien desc ")
    List<HoaDon> findHoaDonByMultipleParamsAPITongTien(
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            @Param("trangThai") Integer trangThai,
            @Param("loaiHoaDon") Integer loaiHoaDon,
            @Param("textSearch") String textSearch
    );

    @Query("SELECT c FROM HoaDon c WHERE" +
            "(:ngayBatDau IS NULL OR c.createdDate >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR c.createdDate <= :ngayKetThuc) AND " +
            "(:loaiHoaDon IS NULL OR c.loaiHoaDon = :loaiHoaDon) AND " +
            "((:textSearch IS NULL OR c.maHoaDOn like :textSearch) or" +
            "(:textSearch IS NULL OR c.soDienThoai like :textSearch) or" +
            "(:textSearch IS NULL OR c.taiKhoan.ten like :textSearch) or " +
            "(:textSearch IS NULL OR c.tenKhachHang like :textSearch)) AND" +
            "(c.trangThai = :trangThai)" +
            "order by c.maHoaDOn desc ")
    List<HoaDon> findHoaDonByMultipleParamsAPImaHoaDOn(
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            @Param("trangThai") Integer trangThai,
            @Param("loaiHoaDon") Integer loaiHoaDon,
            @Param("textSearch") String textSearch

    );

    // thống kê

    @Query("SELECT h.trangThai, COUNT(h) FROM HoaDon h " +
            "WHERE " +
            "(:ngayBatDau IS NULL OR h.createdDate >= :ngayBatDau) " +
            "AND (:ngayKetThuc IS NULL OR h.createdDate <= :ngayKetThuc) " +
            "GROUP BY h.trangThai")
    List<Object[]> countHoaDonByDateAndStatus(
            @Param("ngayBatDau") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc
    );

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE " +
            "(:ngayBatDau IS NULL OR h.updatedDate >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR h.updatedDate <= :ngayKetThuc) AND h.trangThai = 1")
    Double calculateTongTienByDate(
            @Param("ngayBatDau") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc
    );

    @Query("SELECT COUNT(h) FROM HoaDon h WHERE " +
            "(:ngayBatDau IS NULL OR h.createdDate >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR h.createdDate <= :ngayKetThuc)")
    int countHoaDonByDateRange(
            @Param("ngayBatDau") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc
    );

    @Query(value = "select DATEDIFF(DAY, ngayTao, GETDATE()) from HoaDon where loaiHoaDon = 3 and Id = ?1 ", nativeQuery = true)
    Integer getDateNumberHDO(String idhd);

    @Query(value = "select hd from HoaDon hd where hd.trangThai = ?1 and hd.taiKhoan.email like ?2  order by hd.createdDate desc ")
    List<HoaDon> findByTrangThaiAndKhachHang(Integer trangThai,String email);
}