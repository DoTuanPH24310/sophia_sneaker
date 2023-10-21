package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.UUID;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, UUID> {


    @Query(value = "select obj from Voucher obj where (obj.trangThai <> 3) " +
            "and (?1 is null or obj.trangThai =?1) " +
            "and (?2 is null or obj.ma like ?2 or obj.ten like ?2)")
    Page<Voucher> locVaTimKiem(Integer tt, String txt,Pageable pageable);

    @Query(value = "select obj from Voucher obj where obj.trangThai <> 3")
    Page<Voucher> findAll(Pageable pageable);
}
