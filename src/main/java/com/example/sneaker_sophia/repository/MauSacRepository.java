package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, UUID> {
    MauSac findMauSacByTen(String ten);

    @Query(value = "SELECT MauSac.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN MauSac ON ChiTietGiay.IdMauSac = MauSac.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<MauSac> findMauSacsByIdChiTietGiay(UUID uuid);

}
