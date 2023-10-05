package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChiTietGiayRepository extends JpaRepository<ChiTietGiay, UUID> {
//    public Page<ChiTietGiay> findByKeyword(String keyword, Pageable pageable);

}
