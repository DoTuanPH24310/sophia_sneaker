package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Anh;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.AnhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
@Service
public class AnhService {
    @Autowired
    AnhRepository anhRepository;

    public List<Anh> getAll() {
        return anhRepository.findAll();
    }
    public void save(Anh anh) {
        anhRepository.save(anh);
    }
    public Anh getOne(String id) {
        return anhRepository.findById(id).get();
    }

    public void delete(String id) {
        anhRepository.deleteById(id);
    }

    public List<Anh> anhsFindIdChitietGiay(ChiTietGiay chiTietGiay) {
        return anhRepository.findAnhsByChiTietGiay(chiTietGiay);
    }
    @Transactional
    public void deleteAnhByChiTietGiay(ChiTietGiay giay){
        anhRepository.deleteAllByChiTietGiay(giay);
    }
}
