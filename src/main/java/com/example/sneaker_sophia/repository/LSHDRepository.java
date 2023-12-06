package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("lshdRepository")
public interface LSHDRepository extends JpaRepository<LichSuHoaDon, String> {

    @Query(value = "select lshd from LichSuHoaDon lshd where lshd.hoaDon.id = ?1 order by lshd.createdDate asc ")
    List<LichSuHoaDon> getLSHDBYIdhd(String idhd);

}