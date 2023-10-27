package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HangService {
    @Autowired
    HangRepository hangRepository;


    public List<Hang> getAll() {
        return hangRepository.findAll();
    }
    public Hang getOne(UUID uuid) {
        return hangRepository.findById(uuid).get();
    }

    public Hang findByTen(String ten){
        return hangRepository.findHangByTen(ten);
    }
}