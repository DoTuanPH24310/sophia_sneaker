package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class KichCoService2 {
    @Autowired
    KichCoRepository kichCoRepository;
    public KichCo findByTen(String ten){
        return kichCoRepository.findKichCoByTen(ten);
    }

    public KichCo findKichCosByIdChiTietGiay(UUID uuid){
        return kichCoRepository.findKichCosByIdChiTietGiay(uuid);
    }
}
