package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiTamChu extends JpaRepository<DiaChi, String> {
    @Query("SELECT CONCAT(d.tinh, ', ', d.quanHuyen,', ', d.phuongXa, ', ', d.diaChiCuThe) FROM DiaChi d WHERE d IN :diaChiList")
    String taoDiaChiString(@Param("diaChiList") List<DiaChi> diaChiList);

}
