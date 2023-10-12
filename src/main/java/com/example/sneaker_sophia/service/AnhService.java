package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Anh;

import java.util.List;
import java.util.UUID;

public interface AnhService {
    List<Anh> getAll();
    void save(Anh anh);
    Anh getOne(UUID id);

    void delete(UUID id);
}
