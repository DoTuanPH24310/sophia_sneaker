package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class HangService2 {
    @Autowired
    HangRepository hangRepository;
    public Hang findByTen(String ten){
        return hangRepository.findHangByTen(ten);
    }

    public Hang findHangsByIdChiTietGiay(UUID uuid){
        return hangRepository.findHangsByIdChiTietGiay(uuid);
    }

//    public UUID getIdByTen(String ten){
//        return hangRepository.findIdByName(ten);
//    }
}
