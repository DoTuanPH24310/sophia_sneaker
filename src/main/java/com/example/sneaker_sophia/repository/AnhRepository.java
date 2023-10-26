package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Anh;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface AnhRepository extends JpaRepository<Anh, UUID> {
    List<Anh> findAnhsByChiTietGiay(ChiTietGiay chiTietGiay);

}
