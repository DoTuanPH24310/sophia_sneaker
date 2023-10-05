package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public interface ChiTietGiayService {
    List<ChiTietGiay> getAll();
    void add(ChiTietGiay chiTietGiay);

}
