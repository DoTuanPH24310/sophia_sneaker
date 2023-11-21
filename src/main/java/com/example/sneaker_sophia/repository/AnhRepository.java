package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Anh;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("anhRepository")
public interface AnhRepository extends JpaRepository<Anh, String> {
    List<Anh> findAnhsByChiTietGiay(ChiTietGiay chiTietGiay);

    void deleteAllByChiTietGiay(ChiTietGiay giay);

    //    cuongdv
    @Query(value = "select a.duongDan from Anh a join ChiTietGiay CTG on a.ChiTietGiay = CTG.Id where CTG.Id = ?1 and anhChinh  = 1", nativeQuery = true)
    String getAnhChinhByIdctg(UUID idctg);
}
