package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("hoaDonRepository")
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    @Query(value = "  Select COUNT(MaHoaDon) from HoaDon", nativeQuery = true)
    Integer soHD();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 2 order by hd.maHoaDOn")
    List<HoaDon> getHoaDonByTrangThai();

    @Query(value = "select count(maHoaDOn) from HoaDon where trangThai = 2", nativeQuery = true)
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
            "(:textSearch IS NULL OR c.maHoaDOn like :textSearch) AND" +
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
            "(:textSearch IS NULL OR c.maHoaDOn like :textSearch) AND" +
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
            "(:textSearch IS NULL OR c.maHoaDOn like :textSearch) AND" +
            "(c.trangThai = :trangThai)" +
            "order by c.maHoaDOn desc ")
    List<HoaDon> findHoaDonByMultipleParamsAPImaHoaDOn(
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            @Param("trangThai") Integer trangThai,
            @Param("loaiHoaDon") Integer loaiHoaDon,
            @Param("textSearch") String textSearch

    );
}