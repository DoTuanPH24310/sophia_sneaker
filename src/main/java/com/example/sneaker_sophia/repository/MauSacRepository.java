package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.MauSac;
import com.example.sneaker_sophia.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface MauSacRepository extends JpaRepository<MauSac, UUID> {
    @Query(value = "select * from MauSac", nativeQuery = true)
    Page<MauSac> getAll(Pageable pageable);

    @Query("SELECT g FROM MauSac g WHERE g.ten LIKE %:txtSearch%")
    Page<MauSac> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM MauSac g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<MauSac> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);
}
