package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.KichCo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface KichCoRepository extends JpaRepository<KichCo, UUID> {
    @Query(value = "select * from KichCo", nativeQuery = true)
    Page<KichCo> getAll(Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch%")
    Page<KichCo> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<KichCo> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);
}
