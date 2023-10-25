package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.KichCo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface KichCoRepository extends JpaRepository<KichCo, UUID> {
    KichCo findKichCoByTen(String ten);
}
