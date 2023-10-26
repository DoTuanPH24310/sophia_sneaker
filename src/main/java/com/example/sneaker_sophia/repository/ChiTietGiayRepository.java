package com.example.sneaker_sophia.repository;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = "select ma from ChiTietGiay where id =?1",nativeQuery = true)
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
    public Page<ChiTietGiay> findByKeyword(String keyword, Pageable pageable);

    public Page<ChiTietGiay> findByGiay_TenContainingIgnoreCase(String tenSanPham, Pageable pageable);
    @Query("""
        SELECT ctsp
        FROM ChiTietGiay ctsp
        WHERE (LOWER(CONCAT(ctsp.ma, ctsp.giay.ten)) LIKE %?1%)
        AND (ctsp.giay.ten LIKE %?2%)
    """)
    public Page<ChiTietGiay> findByMaAndKeyWord(String keyword, String productName, Pageable pageable);
}

