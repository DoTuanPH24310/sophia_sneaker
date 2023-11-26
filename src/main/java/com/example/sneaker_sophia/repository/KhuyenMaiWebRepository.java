package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KhuyenMaiWebRepository extends JpaRepository<Voucher, UUID> {
    
}


