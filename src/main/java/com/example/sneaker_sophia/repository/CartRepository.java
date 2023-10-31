package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<GioHang, UUID> {

}
