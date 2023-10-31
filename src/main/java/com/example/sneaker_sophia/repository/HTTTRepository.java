package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("htttRepository")
public interface HTTTRepository extends JpaRepository<HinhThucThanhToan, String> {
}
