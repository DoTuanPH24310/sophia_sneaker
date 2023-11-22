package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Giay;
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
public interface DeGiayRepository extends JpaRepository<DeGiay, UUID> {

    @Query(value = "select * from DeGiay", nativeQuery = true)
    Page<DeGiay> getAll(Pageable pageable);

    @Query("SELECT g FROM DeGiay g WHERE g.ten LIKE %:txtSearch%")
    Page<DeGiay> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM DeGiay g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<DeGiay> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    DeGiay findDeGiayByTen(String ten);
    @Query(value = "SELECT DeGiay.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN DeGiay ON ChiTietGiay.IdDeGiay = DeGiay.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<DeGiay> findDeGiaysByIdChiTietGiay(UUID uuid);

    @Query(value = "select DeGiay.* from DeGiay\n" +
            "join ChiTietGiay on DeGiay.Id = ChiTietGiay.IdDeGiay\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    DeGiay findDeGiaysByIdChiTiet(UUID uuid);

    // cuogndv
    List<DeGiay> findByTrangThaiEquals(Integer trangThai);
}
