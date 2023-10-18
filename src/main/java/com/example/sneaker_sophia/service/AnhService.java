package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Anh;
import com.example.sneaker_sophia.entity.ChiTietGiay;

import java.util.List;
import java.util.UUID;

public interface AnhService {
    List<Anh> getAll();
    void save(Anh anh);
    Anh getOne(UUID id);

    void delete(UUID id);

    List<Anh> anhsFindIdChitietGiay(ChiTietGiay chiTietGiay);
}
