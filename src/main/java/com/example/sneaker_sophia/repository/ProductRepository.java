package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.dto.FillterDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ChiTietGiay, UUID> {
    List<ChiTietGiay> findByHang_Ten(String hang);
    List<ChiTietGiay> findByGiaLessThanEqual(Double gia);
    List<ChiTietGiay> findByLoaiGiay_Ten(String loai);
    List<ChiTietGiay> findByDeGiay_Ten(String deGiay);
    List<ChiTietGiay> findByKichCo_Ten(String kichCo);
    List<ChiTietGiay> findByMauSac_Ten(String mauSac);
    @Query("SELECT c, SUM(hdc.soLuong) AS totalQuantitySold " +
            "FROM ChiTietGiay c " +
            "JOIN c.chiTietGiayList hdc " +
            "GROUP BY c " +
            "ORDER BY totalQuantitySold DESC")
    List<Object[]> findTop10BestSelling();
}
