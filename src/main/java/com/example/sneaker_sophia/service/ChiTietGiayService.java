package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ChiTietGiayService {
    List<ChiTietGiay> getAll();
    void save(ChiTietGiay chiTietGiay);
    ChiTietGiay getOne(UUID id);

    void delete(UUID id);
}
