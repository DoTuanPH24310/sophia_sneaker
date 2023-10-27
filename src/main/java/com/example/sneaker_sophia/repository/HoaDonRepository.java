package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("hoaDonRepository")
public interface HoaDonRepository extends JpaRepository<HoaDon, String> {
    @Query(value = "  Select COUNT(MaHoaDon) from HoaDon", nativeQuery = true)
    Integer soHD();

    @Query(value = "select hd from HoaDon hd where hd.trangThai = 2 order by hd.maHoaDOn")
    List<HoaDon> getHoaDonByTrangThai();

    @Query(value = "select count(maHoaDOn) from HoaDon where trangThai = 2", nativeQuery = true)
    Integer countHoaDonByTrangThai();
}