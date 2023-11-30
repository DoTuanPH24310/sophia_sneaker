package com.example.sneaker_sophia.repository;
import com.example.sneaker_sophia.entity.CTG_KhuyenMai;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.IDVoucher;
import com.example.sneaker_sophia.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Repository
public interface CTG_KhuyenMaiRespsitory extends JpaRepository<CTG_KhuyenMai, IDVoucher> {


    @Modifying
    @Transactional
    @Query(value="delete from CTG_KhuyenMai obj where obj.id.voucher =?1")
    void deleteByIdKM(Voucher vc);


    @Query(value = "select obj.id.chiTietGiay.id from CTG_KhuyenMai obj where obj.id.voucher = ?1")
    List<String> findIdCTG(Voucher vc);

    @Query(value = "select obj.id.chiTietGiay from CTG_KhuyenMai obj where obj.id.voucher = ?1")
    List<ChiTietGiay> findCTG(Voucher vc);





//    @Query(value="delete from CTG_KhuyenMai where CTG_KhuyenMai.IdKhuyenMai =?1",nativeQuery = true)
//    void deleteByIdKM(Voucher vc);

}
