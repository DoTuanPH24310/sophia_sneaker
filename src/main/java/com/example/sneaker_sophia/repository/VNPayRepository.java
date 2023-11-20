package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.VNPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VNPayRepository extends JpaRepository<VNPay, UUID> {
}
