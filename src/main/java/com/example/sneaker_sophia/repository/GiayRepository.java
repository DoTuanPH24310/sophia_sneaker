package com.example.sneaker_sophia.repository;


import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GiayRepository extends JpaRepository<Giay, UUID> {

    @Query(value = "select LOWER(id) from Giay",nativeQuery = true)
    List<String> finAllId();


}
