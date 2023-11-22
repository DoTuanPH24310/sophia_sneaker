package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.entity.KichCo;
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

    @Query(value = "select * from KichCo", nativeQuery = true)
    Page<KichCo> getAll(Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch%")
    Page<KichCo> searchWithoutTrangThai(@Param("txtSearch") String txtSearch, Pageable pageable);

    @Query("SELECT g FROM KichCo g WHERE g.ten LIKE %:txtSearch% AND (g.trangThai = :trangThai OR :trangThai IS NULL)")
    Page<KichCo> searchAndFilter(@Param("txtSearch") String txtSearch, @Param("trangThai") String trangThai, Pageable pageable);

//    @Query(value = "select obj from KichCo obj where (obj.trangThai <> 3) " +
//            "and (?1 is null or obj.trangThai =?1) " +
//            "and (?2 is null or obj.ma like ?2 or obj.ten like ?2)")
//    Page<KichCo> locVaTimKiem(Integer tt, String txt,Pageable pageable);

    KichCo findKichCoByTen(String ten);

    @Query(value = "SELECT KichCo.*\n" +
            "FROM ChiTietGiay\n" +
            "         JOIN KichCo ON ChiTietGiay.IdKichCo = KichCo.Id\n" +
            "         JOIN Giay ON ChiTietGiay.IdGiay = Giay.Id\n" +
            "WHERE Giay.Id = ?", nativeQuery = true)
    List<KichCo> findKichCosByIdChiTietGiay(UUID uuid);

    List<KichCo> findByTrangThaiEquals(Integer trangThai);
}
