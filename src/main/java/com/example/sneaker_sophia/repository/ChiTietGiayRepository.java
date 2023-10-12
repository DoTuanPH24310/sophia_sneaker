package com.example.sneaker_sophia.repository;


import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChiTietGiayRepository extends JpaRepository<ChiTietGiay, UUID> {
    @Query(value = "SELECT obj FROM ChiTietGiay obj WHERE obj.giay.id IN :listId")
    List<ChiTietGiay> findAllByIdGiay(List<UUID> listId);
}
