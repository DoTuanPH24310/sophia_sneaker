package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietGiayRepository extends JpaRepository<ChiTietGiay,String> {
//    public Page<ChiTietGiay> findByKeyword(String keyword, Pageable pageable);

}
