package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface ChiTietGiayService {
    public static final int PRODUCT_DETAIL_PER_PAGE = 10;
    List<ChiTietGiay> getAll();
    void save(ChiTietGiay chiTietGiay);
    ChiTietGiay getOne(UUID id);

    void delete(UUID id);

    Page<ChiTietGiay> findAll(Pageable pageable);

    Page<ChiTietGiay> listByPageAndProductName(int pageNum, String sortField, String sortDir, String keyword, String productName);

}
