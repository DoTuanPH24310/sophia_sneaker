package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.Anh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("anhRepository")
public interface AnhRepository extends JpaRepository<Anh, String> {
    @Query(value = "select a.duongDan from Anh a join ChiTietGiay CTG on a.ChiTietGiay = CTG.Id where CTG.Id = ?1 and anhChinh  = 1", nativeQuery = true)
    String getAnhChinhByIdctg(UUID idctg);
}
