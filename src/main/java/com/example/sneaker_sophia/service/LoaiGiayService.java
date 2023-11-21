package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.repository.LoaiGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LoaiGiayService {
    @Autowired
    LoaiGiayRepository loaiGiayRepository;

    public List<LoaiGiay> getAll() {
        return loaiGiayRepository.findAll();
    }

    public LoaiGiay getOne(UUID uuid) {
        return loaiGiayRepository.findById(uuid).get();
    }

    public LoaiGiay findByTen(String ten){
        return loaiGiayRepository.findLoaiGiayByTen(ten);
    }
//cuongdv
    public List<LoaiGiay> findByTrangThaiEquals(Integer tt) {
        return loaiGiayRepository.findByTrangThaiEquals(tt);
    }

}
