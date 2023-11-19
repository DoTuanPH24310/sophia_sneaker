package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("htttRepository")
public interface HTTTRepository extends JpaRepository<HinhThucThanhToan, String> {

    @Query(value = "select httt from HinhThucThanhToan httt where httt.hoaDon.id = ?1")
    HinhThucThanhToan getHTTTByIdhd(String idhd);

}
