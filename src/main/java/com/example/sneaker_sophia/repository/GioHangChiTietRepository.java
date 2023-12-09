package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.IdGioHangChiTiet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, IdGioHangChiTiet> {
    GioHangChiTiet findById_GioHangAndId_ChiTietGiay(GioHang gioHang, ChiTietGiay chiTietGiay);
    List<GioHangChiTiet> findById_GioHang(GioHang gioHang);

    @Query("SELECT g FROM GioHangChiTiet g WHERE g.id.chiTietGiay.id = :chiTietGiayId")
    List<GioHangChiTiet> findByChiTietGiayId(@Param("chiTietGiayId") UUID chiTietGiayId);

    @Query(value = "SELECT COUNT(*) FROM GioHangChiTiet ght\n" +
            "JOIN GioHang gh ON ght.IdGioHang = gh.Id\n" +
            "JOIN TaiKhoan tk ON gh.IdTaiKhoan = tk.Id\n" +
            "WHERE tk.Email = :email", nativeQuery = true)
    Long countProduct(String email);

    @Query("SELECT c.soLuong FROM GioHangChiTiet c WHERE c.id.gioHang.id = :gioHangId AND c.id.chiTietGiay.id = :chiTietGiayId")
    Integer getAvailableQuantity(@Param("gioHangId") UUID gioHangId, @Param("chiTietGiayId") UUID chiTietGiayId);

    @Modifying
    @Query("UPDATE GioHangChiTiet c SET c.soLuong = c.soLuong + 1 WHERE c.id.gioHang.id = :gioHangId AND c.id.chiTietGiay.id = :chiTietGiayId")
    @Transactional
    void increaseQuantity(@Param("gioHangId") UUID gioHangId, @Param("chiTietGiayId") UUID chiTietGiayId);

    @Modifying
    @Query("UPDATE GioHangChiTiet c SET c.soLuong = c.soLuong - 1 WHERE c.id.gioHang.id = :gioHangId AND c.id.chiTietGiay.id = :chiTietGiayId")
    @Transactional
    void decreaseQuantity(@Param("gioHangId") UUID gioHangId, @Param("chiTietGiayId") UUID chiTietGiayId);
}
