package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("diaChiRepository")
public interface DiaChiRepository extends JpaRepository<DiaChi, String> {
    @Query(value = "select * from DiaChi where IdTaiKhoan = ?1", nativeQuery = true)
    DiaChi getDiaChiByIdTaiKhoan(String idTaiKhoan);

    @Query(value = "select tk, dc from TaiKhoan tk join DiaChi dc on dc.taiKhoan.id = tk.id where tk.id = ?1")
    DiaChi getNhanVienDTOById(String id);
}
