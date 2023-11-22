package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class DeGiayService2 {
    @Autowired
    DeGiayRepository deGiayRepository;
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
