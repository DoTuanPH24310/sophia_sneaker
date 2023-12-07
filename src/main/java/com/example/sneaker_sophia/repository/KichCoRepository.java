package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, UUID> {

    @Query("SELECT g FROM KichCo g WHERE g.trangThai IN (0, 1)")
    Page<KichCo> getAll(Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch% AND g.trangThai IN (0, 1)")
    Page<KichCo> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch% AND g.trangThai = :trangThai")
    Page<KichCo> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    boolean existsKichCoByMa(String ma);
    boolean existsKichCoByTen(String ten);

    KichCo findKichCoByTen(String ten);


    List<KichCo> findByTrangThaiEquals(Integer trangThai);

    @Query(value = "select * from KichCo where trangThai= 0 or trangThai =1",nativeQuery = true)
    List<KichCo> finAllTrangThai();

    @Query("SELECT g FROM KichCo g WHERE g.trangThai IN (0)")
    List<KichCo> findAll();
}
