package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, UUID> {
    KichCo findKichCoByTen(String ten);

    @Query(value = "SELECT KichCo.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN KichCo ON ChiTietGiay.IdKichCo = KichCo.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<KichCo> findKichCosByIdChiTietGiay(UUID uuid);
}
