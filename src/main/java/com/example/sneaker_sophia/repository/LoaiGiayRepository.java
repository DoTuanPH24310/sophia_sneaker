package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.entity.LoaiGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoaiGiayRepository extends JpaRepository<LoaiGiay, UUID> {
    LoaiGiay findLoaiGiayByTen(String ten);

    @Query(value = "select LoaiGiay.* from LoaiGiay\n" +
            "join ChiTietGiay on LoaiGiay.Id = ChiTietGiay.IdLoaiGiay\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    LoaiGiay findLoaiGiaysByIdChiTietGiay(UUID uuid);
}
