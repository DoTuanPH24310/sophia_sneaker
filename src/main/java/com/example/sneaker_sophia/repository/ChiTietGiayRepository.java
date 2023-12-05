package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChiTietGiayRepository extends JpaRepository<ChiTietGiay, UUID> {
    @Query(value = "SELECT obj FROM ChiTietGiay obj WHERE obj.giay.id IN :listId")
    List<ChiTietGiay> findAllByIdGiay(List<UUID> listId);

    @Query(value = "SELECT ChiTietGiay.*\n" +
            "           FROM ChiTietGiay\n" +
            "           INNER JOIN Giay ON Giay.Id = ChiTietGiay.IdGiay\n" +
            "           WHERE Giay.Id  in ?1\n" +
            "            AND NOT EXISTS (\n" +
            "            SELECT *\n" +
            "             FROM CTG_KhuyenMai inner join KhuyenMai KM on KM.Id = CTG_KhuyenMai.IdKhuyenMai\n" +
            "             WHERE CTG_KhuyenMai.IdCTG = ChiTietGiay.Id and ( KM.trangThai =1 or KM.trangThai =0)\n" +
            "            and (?2 is null or KM.Id <> ?2)\n" +
            ");", nativeQuery = true)
    List<ChiTietGiay> findAllByIdGiay(List<UUID> listId, UUID idKM);


    @Query(value = "SELECT obj.id FROM ChiTietGiay obj WHERE obj.giay.id IN :listId")
    List<String> findIdByIdGiay(List<UUID> listId);

    List<ChiTietGiay> findAllByTrangThaiEquals(Integer trangThai);
    @Query(value = "select * from ChiTietGiay  where trangThai = ?1 order by ngayTao desc",nativeQuery = true)
    List<ChiTietGiay> findAllAndOrder(Integer trangThai);
    @Query(value = "select ma from ChiTietGiay where id =?1", nativeQuery = true)
    String findMaByIdCTG(UUID id);
    // Hàm tìm kiếm theo cả keyword và tên sản phẩm

    @Query("""
                SELECT ctsp
                FROM ChiTietGiay ctsp
                WHERE (UPPER(ctsp.ma) LIKE %?1%
                    OR UPPER(ctsp.hang.ten) LIKE %?1%
                    OR UPPER(ctsp.ten) LIKE %?1%
                    OR UPPER(ctsp.mauSac.ten) LIKE %?1%
                    OR UPPER(ctsp.kichCo.ten) LIKE %?1%
                    OR UPPER(ctsp.deGiay.ten) LIKE %?1%
                    OR UPPER(ctsp.loaiGiay.ten) LIKE %?1%
                    OR UPPER(ctsp.giay.ten) LIKE %?1%)
                    AND (?2 = -1 OR ctsp.trangThai = ?2)
            """)
    Page<ChiTietGiay> findByKeyword(String keyword, String trangThai, Pageable pageable);


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
            "(:trangThai IS NULL OR c.trangThai = :trangThai OR (:trangThai = -1 AND c.trangThai IS NOT NULL)) AND " +
            "(:giaMin IS NULL OR c.gia >= :giaMin) AND " +
            "(:giaMax IS NULL OR c.gia <= :giaMax) ")
    Page<ChiTietGiay> findChiTietGiayByMultipleParams(
            @Param("giay") Giay giay,
            @Param("deGiay") DeGiay deGiay,
            @Param("hang") Hang hang,
            @Param("loaiGiay") LoaiGiay loaiGiay,
            @Param("mauSac") MauSac mauSac,
            @Param("kichCo") KichCo kichCo,
            @Param("trangThai") String trangThai,
            @Param("giaMin") Double giaMin,
            @Param("giaMax") Double giaMax,
            Pageable pageable
    );


    @Query("SELECT MAX(c.ma) FROM ChiTietGiay c")
        Integer findMaxMa();
        @Query(value = "SELECT ChiTietGiay.* FROM dbo.ChiTietGiay WHERE ChiTietGiay.IdGiay = (SELECT IdGiay FROM dbo.ChiTietGiay WHERE Id = ?)", nativeQuery = true)
        List<ChiTietGiay> findChiTietGiaysById(UUID uuid);


    @Query("SELECT c FROM ChiTietGiay c WHERE " +
            "(c.giay = :giay) AND " +
            "(c.deGiay = :deGiay) AND " +
            "(c.hang = :hang) AND " +
            "(c.loaiGiay = :loaiGiay) AND " +
            "(c.mauSac = :mauSac)")
    List<ChiTietGiay> getChiTietGiaysByIdChiTietGiay(
            @Param("giay") Giay giay,
            @Param("deGiay") DeGiay deGiay,
            @Param("hang") Hang hang,
            @Param("loaiGiay") LoaiGiay loaiGiay,
            @Param("mauSac") MauSac mauSac);


    Page<ChiTietGiay> findAll(Specification<ChiTietGiay> spec, Pageable pageable);

    @Query("SELECT a.chiTietGiay.id FROM Anh a WHERE a.id = :anhId")
    UUID findChiTietGiayIdByAnhId(@Param("anhId") String anhId);
    //cuongdv

    // 29/10 cuongdv
    @Query(value = "select Id from ChiTietGiay where ma = ?1", nativeQuery = true)
    UUID getIdCTGByMa(String maCTG);


    @Query(value = "select soLuong from ChiTietGiay where ma =?1", nativeQuery = true)
    Integer findSoLuongTon(String ma);

    @Query(value = "select ct from ChiTietGiay ct where ct.qrCode = ?1")
    ChiTietGiay getChiTietGiayByQrCode(String qrcode);


    @Query(value = "select soLuong from ChiTietGiay where qrCode =?1", nativeQuery = true)
    Integer findSoLuongTonByQrCode(String qr);

    //    17/11 + 18/11
    @Query(value = "select sum(KM.phanTramGiam) from CTG_KhuyenMai ctg_km\n" +
            "join KhuyenMai KM on ctg_km.IdKhuyenMai = KM.Id where  IdCTG = ?1 and KM.trangThai = 1 and KM.soLuong > 0", nativeQuery = true)
    Integer tongKMByIdctg(UUID idctg);


    @Query("SELECT c FROM ChiTietGiay c WHERE " +
            "(:giay IS NULL OR c.giay.id = :giay) AND " +
            "(:deGiay IS NULL OR c.deGiay.id = :deGiay) AND " +
            "(:hang IS NULL OR c.hang.id = :hang) AND " +
            "(:loaiGiay IS NULL OR c.loaiGiay.id = :loaiGiay) AND " +
            "(:mauSac IS NULL OR c.mauSac.id = :mauSac) AND" +
            "(:kichCo IS NULL OR c.kichCo.id = :kichCo) AND ((:textSearch IS NULL OR c.ma like :textSearch) or (:textSearch IS NULL OR c.giay.ten like :textSearch)) AND" +
            "(c.trangThai = 0)")
    List<ChiTietGiay> findChiTietGiayByMultipleParamsAPI(
            @Param("giay") UUID idGiay,
            @Param("deGiay") UUID idDeGiay,
            @Param("hang") UUID idHang,
            @Param("loaiGiay") UUID idLoaiGiay,
            @Param("mauSac") UUID idMauSac,
            @Param("kichCo") UUID idKichCo,
            @Param("textSearch") String textSearch
    );

    @Query("SELECT c.soLuong FROM ChiTietGiay c WHERE c.id = :chiTietGiayId")
    Integer getProductQuantityById(@Param("chiTietGiayId") UUID chiTietGiayId);

    //xóa mềm chi tiết giày
    @Transactional
    @Modifying
    @Query(value = "UPDATE ChiTietGiay SET trangThai = 1 WHERE id = :id", nativeQuery = true)
    void updateTrangThaiTo1ById(UUID id);

    ChiTietGiay findByMa(String ma);

    @Query("SELECT c.ma, CONCAT(g.ten, ' ', c.ten, ' ', h.ten, ' ', m.ten, ' ', k.ten) AS concatenatedInfo, c.soLuong " +
            "FROM ChiTietGiay c " +
            "JOIN c.giay g " +
            "JOIN c.hang h " +
            "JOIN c.mauSac m " +
            "JOIN c.kichCo k " +
            "WHERE c.soLuong < :soLuongInput " +
            "ORDER BY c.soLuong ASC")
    List<Object[]> getConcatenatedInfoAndSoLuongBySoLuong(@Param("soLuongInput") int soLuongInput);

    @Query("SELECT c FROM ChiTietGiay c " +
            "WHERE c.giay = :giay " +
            "   AND c.deGiay = :deGiay " +
            "   AND c.hang = :hang " +
            "   AND c.loaiGiay = :loaiGiay " +
            "   AND c.mauSac = :mauSac")
    List<ChiTietGiay> findSimilarChiTietGiay(
            @Param("giay") Giay giay,
            @Param("deGiay") DeGiay deGiay,
            @Param("hang") Hang hang,
            @Param("loaiGiay") LoaiGiay loaiGiay,
            @Param("mauSac") MauSac mauSac
    );
}

