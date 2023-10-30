package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.entity.MauSac;
import com.example.sneaker_sophia.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MauSacService {
    @Autowired
    MauSacRepository mauSacRepository;

    public List<MauSac> getAll() {
        return mauSacRepository.findAll();
    }
    public MauSac getOne(UUID uuid) {
        return mauSacRepository.findById(uuid).get();
    }

    public MauSac findByTen(String ten){
        return mauSacRepository.findMauSacByTen(ten);
    }

    public List<MauSac> findMauSacsByIdChiTietGiay(UUID uuid) {
        return mauSacRepository.findMauSacsByIdChiTietGiay(uuid);
    }
}
