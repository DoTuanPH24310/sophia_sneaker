package com.example.sneaker_sophia.repository;
import com.example.sneaker_sophia.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VoucherRepository extends JpaRepository<Voucher, String> {
    @Query(value = "select * from Voucher where (?1 is null or FREETEXT((ma,ten),?1))" +
            "and (?2 is null or trangthai =?2)"
            ,nativeQuery = true)
    Page<Voucher> searchAndFilter(String text, String trangThai, Pageable pageable);



}
