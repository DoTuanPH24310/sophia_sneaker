package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DeGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeGiayRepository extends JpaRepository<DeGiay, UUID> {
    DeGiay findDeGiayByTen(String ten);
    @Query(value = "SELECT DeGiay.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN DeGiay ON ChiTietGiay.IdDeGiay = DeGiay.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<DeGiay> findDeGiaysByIdChiTietGiay(UUID uuid);
}
