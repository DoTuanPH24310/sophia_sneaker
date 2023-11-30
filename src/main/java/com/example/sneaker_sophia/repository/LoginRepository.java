package com.example.sneaker_sophia.repository;

import com.example.sneaker_sophia.entity.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoginRepository extends JpaRepository<TaiKhoan, String> {
    TaiKhoan findByEmail(String email);
    boolean existsByEmail(String email);
    TaiKhoan getTaiKhoanByEmail(String email);

}
