package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<DiaChi, String> {
    List<DiaChi> findByTaiKhoan_Email(String email);
}
