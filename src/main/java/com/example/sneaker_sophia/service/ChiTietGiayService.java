package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public interface ChiTietGiayService {
//    public static final int PRODUCT_DETAIL_PER_PAGE = 10;
    List<ChiTietGiay> getAll();
//    Page<ChiTietGiay> listByPageAndProductName(int pageNum, String sortField, String sortDir, String keyword, String productName);

}
