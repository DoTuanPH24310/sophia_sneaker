package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.VaiTro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("vaiTroRepository")
public interface VaiTroRepository extends JpaRepository<VaiTro, String> {
    @Query(value = "select id from VaiTro where ten = 'Nhan Vien'", nativeQuery = true)
    String getIdByTen();
}