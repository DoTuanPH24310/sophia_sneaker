package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.Hang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HangRepository extends JpaRepository<Hang, UUID> {
    Hang findHangByTen(String ten);

    @Query(value = "select Hang.* from Hang\n" +
            "join ChiTietGiay on Hang.Id = ChiTietGiay.IdHang\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    Hang findHangsByIdChiTietGiay(UUID uuid);
}
