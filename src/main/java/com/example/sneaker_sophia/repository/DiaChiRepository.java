package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("diaChiRepository")
public interface DiaChiRepository extends JpaRepository<DiaChi, String> {
    @Query(value = "select dc from DiaChi dc where dc.taiKhoan.id = ?1 and  dc.diaChiMacDinh = 1")
    DiaChi getDiaChiByIdTaiKhoan(String idTaiKhoan);

    @Query(value = "select tk, dc from TaiKhoan tk join DiaChi dc on dc.taiKhoan.id = tk.id where tk.id = ?1")
    DiaChi getNhanVienDTOById(String id);

    // 30-10

    @Query(value = "select * from DiaChi where IdTaiKhoan =?1",nativeQuery = true)
    List<DiaChi> findListTKById(UUID id);

    @Query(value = "select * from DiaChi where IdTaiKhoan =?1",nativeQuery = true)
    List<DiaChi> findListTKByIdKH(String id);

    @Query(value = "select  * from DiaChi where IdTaiKhoan = ?1 and diaChiMacDinh = 1 ",nativeQuery = true)
    DiaChi findListTKByIdKHAndDCMD(String idkh);

}