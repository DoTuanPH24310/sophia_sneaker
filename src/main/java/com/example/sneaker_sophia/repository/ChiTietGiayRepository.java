package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChiTietGiayRepository extends JpaRepository<ChiTietGiay, UUID> {
    @Query(value = "SELECT obj FROM ChiTietGiay obj WHERE obj.giay.id IN :listId")
    List<ChiTietGiay> findAllByIdGiay(List<UUID> listId);

    @Query(value = "SELECT obj.id FROM ChiTietGiay obj WHERE obj.giay.id IN :listId")
    List<String> findIdByIdGiay(List<UUID> listId);

    List<ChiTietGiay> findAllByTrangThaiEquals(Integer trangThai);

    @Query(value = "select ma from ChiTietGiay where id =?1", nativeQuery = true)
    String findMaByIdCTG(UUID id);
    // Hàm tìm kiếm theo cả keyword và tên sản phẩm

    @Query("""
                SELECT ctsp
                FROM ChiTietGiay ctsp
                WHERE UPPER(ctsp.ma) LIKE %?1%
                    OR UPPER(ctsp.hang.ten) LIKE %?1%
                    OR UPPER(ctsp.mauSac.ten) LIKE %?1%
                    OR UPPER(ctsp.kichCo.ten) LIKE %?1%
                    OR UPPER(ctsp.deGiay.ten) LIKE %?1%
                    OR UPPER(ctsp.loaiGiay.ten) LIKE %?1%
                    OR UPPER(ctsp.giay.ten) LIKE %?1%
            """)
    Page<ChiTietGiay> findByKeyword(String keyword, Pageable pageable);

    public Page<ChiTietGiay> findByGiay_TenContainingIgnoreCase(String tenSanPham, Pageable pageable);

    @Query("""
                SELECT ctsp
                FROM ChiTietGiay ctsp
                WHERE (LOWER(CONCAT(ctsp.ma, ctsp.giay.ten)) LIKE %?1%)
                AND (ctsp.giay.ten LIKE %?2%)
            """)
    Page<ChiTietGiay> findByMaAndKeyWord(String keyword, String productName, Pageable pageable);

    @Query("SELECT c FROM ChiTietGiay c WHERE " +
            "(:giay IS NULL OR c.giay = :giay) AND " +
            "(:deGiay IS NULL OR c.deGiay = :deGiay) AND " +
            "(:hang IS NULL OR c.hang = :hang) AND " +
            "(:loaiGiay IS NULL OR c.loaiGiay = :loaiGiay) AND " +
            "(:mauSac IS NULL OR c.mauSac = :mauSac) AND " +
            "(:kichCo IS NULL OR c.kichCo = :kichCo) AND " +
            "(:giaMin IS NULL OR c.gia >= :giaMin) AND " +
            "(:giaMax IS NULL OR c.gia <= :giaMax)")
    Page<ChiTietGiay> findChiTietGiayByMultipleParams(
            @Param("giay") Giay giay,
            @Param("deGiay") DeGiay deGiay,
            @Param("hang") Hang hang,
            @Param("loaiGiay") LoaiGiay loaiGiay,
            @Param("mauSac") MauSac mauSac,
            @Param("kichCo") KichCo kichCo,
            @Param("giaMin") Double giaMin,
            @Param("giaMax") Double giaMax,
            Pageable pageable
    );


    @Query("SELECT c FROM ChiTietGiay c WHERE " +
            "(:giay IS NULL OR c.giay.id = :giay) AND " +
            "(:deGiay IS NULL OR c.deGiay.id = :deGiay) AND " +
            "(:hang IS NULL OR c.hang.id = :hang) AND " +
            "(:loaiGiay IS NULL OR c.loaiGiay.id = :loaiGiay) AND " +
            "(:mauSac IS NULL OR c.mauSac.id = :mauSac) AND" +
            "(:kichCo IS NULL OR c.kichCo.id = :kichCo) AND ((:textSearch IS NULL OR c.ma like :textSearch) or (:textSearch IS NULL OR c.giay.ten like :textSearch)) AND" +
            "(c.trangThai = 0)")
    List<ChiTietGiay> findChiTietGiayByMultipleParamsAPI(
            @Param("giay") UUID  idGiay,
            @Param("deGiay") UUID idDeGiay,
            @Param("hang") UUID idHang,
            @Param("loaiGiay") UUID idLoaiGiay,
            @Param("mauSac") UUID idMauSac,
            @Param("kichCo") UUID idKichCo,
            @Param("textSearch") String textSearch
    );


    @Query("SELECT MAX(c.ma) FROM ChiTietGiay c")
    Integer findMaxMa();
    // 29/10 cuongdv
    @Query(value = "select Id from ChiTietGiay where ma = ?1", nativeQuery = true)
    UUID getIdCTGByMa(String maCTG);


    @Query(value = "select soLuong from ChiTietGiay where ma =?1", nativeQuery = true)
    Integer findSoLuongTon(String ma);
}
//         OR UPPER(ctsp.kichCo.ten) LIKE %?1%
//(:kichCo IS NULL OR c.kichCo = :kichCo) AND
//(:kichCo IS NULL OR c.kichCo.id = :kichCo) AND