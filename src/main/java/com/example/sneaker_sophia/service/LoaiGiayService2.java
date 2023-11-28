package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.LoaiGiayRequest;
import com.example.sneaker_sophia.entity.LoaiGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface LoaiGiayService2 {
    List<LoaiGiay> getAll();

    LoaiGiay add(LoaiGiayRequest loaiGiayRequest);

    Optional<LoaiGiay> findOne(UUID id);

    Page<LoaiGiay> fillter(String txtSearch, String trangThai, Pageable pageable);

    LoaiGiay update(UUID id, LoaiGiayRequest loaiGiayRequest);

    LoaiGiay delete(UUID id);

    LoaiGiay getOne(UUID uuid);

    LoaiGiay findByTen(String ten);

    List<LoaiGiay> findByTrangThaiEquals(Integer i);
}
