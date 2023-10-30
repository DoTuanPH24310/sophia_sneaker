package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class KichCoService {
    @Autowired
    KichCoRepository kichCoRepository;
    public List<KichCo> getAll() {
        return kichCoRepository.findAll();
    }
    public KichCo getOne(UUID uuid) {
        return kichCoRepository.findById(uuid).get();
    }

    public KichCo findByTen(String ten){
        return kichCoRepository.findKichCoByTen(ten);
    }

    public List<KichCo> findKichCosByIdChiTietGiay(UUID uuid) {
        return kichCoRepository.findKichCosByIdChiTietGiay(uuid);
    }
}
