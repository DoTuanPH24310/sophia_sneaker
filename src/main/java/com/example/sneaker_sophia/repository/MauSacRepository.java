package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.LoaiGiay;
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
public interface MauSacRepository extends JpaRepository<MauSac, UUID> {

    @Query(value = "select * from MauSac", nativeQuery = true)
    Page<MauSac> getAll(Pageable pageable);

    @Query("SELECT g FROM MauSac g WHERE g.ten LIKE %:txtSearch%")
    Page<MauSac> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM MauSac g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<MauSac> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    MauSac findMauSacByTen(String ten);

    @Query(value = "SELECT MauSac.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN MauSac ON ChiTietGiay.IdMauSac = MauSac.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<MauSac> findMauSacsByIdChiTietGiay(UUID uuid);

    @Query(value = "select MauSac.* from MauSac\n" +
            "join ChiTietGiay on MauSac.Id = ChiTietGiay.IdMauSac\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    MauSac findMauSacsByIdChiTiet(UUID uuid);

    //cuongdv
    List<MauSac> findByTrangThaiEquals(Integer trangThai);
}
