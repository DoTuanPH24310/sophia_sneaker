package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeGiayService {
    @Autowired
    DeGiayRepository deGiayRepository;
    public List<DeGiay> getAll() {
        return deGiayRepository.findAll();
    }

    public DeGiay getOne(UUID uuid) {
        return deGiayRepository.findById(uuid).get();
    }

    public DeGiay findByTen(String ten){
        return deGiayRepository.findDeGiayByTen(ten);
    }

    public List<DeGiay> findDeGiaysByIdChiTietGiay(UUID uuid) {
        return deGiayRepository.findDeGiaysByIdChiTietGiay(uuid);
    }

    public DeGiay findDeGiaysByIdChiTiet(UUID uuid){
        return deGiayRepository.findDeGiaysByIdChiTiet(uuid);
    }
}
