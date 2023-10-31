package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GioHangRepository extends JpaRepository<GioHang, UUID> {
}
