package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.LoaiGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LoaiGiayRepository extends JpaRepository<LoaiGiay, UUID> {
    @Query("SELECT g FROM LoaiGiay g WHERE g.trangThai IN (0, 1)")
    Page<LoaiGiay> getAll(Pageable pageable);

    @Query("SELECT g FROM LoaiGiay g WHERE g.ten LIKE %:txtSearch% AND g.trangThai IN (0, 1)")
    Page<LoaiGiay> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM LoaiGiay g WHERE g.ten LIKE %:txtSearch% AND g.trangThai = :trangThai")
    Page<LoaiGiay> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    boolean existsLoaiGiayByMa(String ma);
    boolean existsLoaiGiayByTen(String ten);

    LoaiGiay findLoaiGiayByTen(String ten);

    @Query(value = "select LoaiGiay.* from LoaiGiay\n" +
            "join ChiTietGiay on LoaiGiay.Id = ChiTietGiay.IdLoaiGiay\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    LoaiGiay findLoaiGiaysByIdChiTietGiay(UUID uuid);

    List<LoaiGiay> findByTrangThaiEquals(Integer trangThai);

    @Query(value = "select * from LoaiGiay where trangThai= 0 or trangThai =1",nativeQuery = true)
    List<LoaiGiay> finAllTrangThai();
}
