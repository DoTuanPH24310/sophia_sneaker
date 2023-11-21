package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.LoaiGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoaiGiayRepository extends JpaRepository<LoaiGiay, UUID> {
    LoaiGiay findLoaiGiayByTen(String ten);
    List<LoaiGiay> findByTrangThaiEquals(Integer trangThai);
}
