package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;
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
    @Query("SELECT g FROM Giay g WHERE g.trangThai IN (0, 1)")
    Page<Giay> getAll(Pageable pageable);

    @Query("SELECT g FROM Giay g WHERE g.ten LIKE %:txtSearch% AND g.trangThai IN (0, 1)")
    Page<Giay> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM Giay g WHERE g.ten LIKE %:txtSearch% AND g.trangThai = :trangThai")
    Page<Giay> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

    @Query(value = "select LOWER(id) from Giay where trangThai= ?1",nativeQuery = true)
    List<String> finAllId(Integer trangThai);

    @Query(value = "select * from Giay where trangThai= 0 or trangThai =1",nativeQuery = true)
    List<Giay> finAllTrangThai();

//    23/11
    @Query(value = "select obj.id from Giay obj where obj.id in :list")
    List<UUID> finGiayByCTG(List<UUID> list);

    List<Giay> findAllByTrangThaiEquals(int trangThai);

    boolean existsGiayByMa(String ma);
    boolean existsGiayByTen(String ten);

    Giay findGiayByTen(String ten);

    @Query(value = "SELECT ChiTietGiay.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?",nativeQuery = true)
    List<ChiTietGiay> findChiTietGiaysById(UUID uuid);

    @Query(value = "select Giay.* from Giay\n" +
            "join ChiTietGiay on Giay.Id = ChiTietGiay.IdGiay\n" +
            "where ChiTietGiay.id =? ", nativeQuery = true)
    Giay findGiaysByIdChiTietGiay(UUID uuid);

    @Query("SELECT g FROM Giay g WHERE g.trangThai IN (0)")
    List<Giay> findAll();
}


