package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Giay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GiayRepository extends JpaRepository<Giay, UUID> {
    @Query(value = "select * from Giay", nativeQuery = true)
    Page<Giay> getAll(Pageable pageable);

    @Query("SELECT g FROM Giay g WHERE g.ten LIKE %:txtSearch%")
    Page<Giay> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM Giay g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<Giay> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    @Query(value = "select LOWER(id) from Giay where trangThai= ?1",nativeQuery = true)
    List<String> finAllId(Integer trangThai);

    List<Giay> findAllByTrangThaiEquals(int trangThai);

    boolean existsGiayByMa(String ma);

    Giay findGiayByTen(String ten);

}


