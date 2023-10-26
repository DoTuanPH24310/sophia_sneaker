package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Hang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HangRepository extends JpaRepository<Hang, UUID> {
    @Query(value = "select * from Hang", nativeQuery = true)
    Page<Hang> getAll(Pageable pageable);

    @Query("SELECT g FROM Hang g WHERE g.ten LIKE %:txtSearch%")
    Page<Hang> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM Hang g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<Hang> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);
}
