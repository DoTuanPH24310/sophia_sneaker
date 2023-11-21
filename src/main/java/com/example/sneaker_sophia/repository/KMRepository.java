package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("kmRepository")
public interface KMRepository extends JpaRepository<Voucher, UUID> {
    @Query(value = "select KM.* from CTG_KhuyenMai ctg_km join KhuyenMai KM on ctg_km.IdKhuyenMai = KM.Id where  IdCTG = ?1 and KM.trangThai = 1", nativeQuery = true)
    List<Voucher> getAllKMByIdctg(UUID idctg);
}