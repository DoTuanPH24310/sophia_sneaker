package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Hang;
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

    public LoaiGiay findHangsByIdChiTietGiay(UUID uuid){
        return loaiGiayRepository.findLoaiGiaysByIdChiTietGiay(uuid);
    }
//cuongdv
    public List<LoaiGiay> findByTrangThaiEquals(Integer tt) {
        return loaiGiayRepository.findByTrangThaiEquals(tt);
    }

    public List<LoaiGiay> finAllTrangThai(){
        return loaiGiayRepository.finAllTrangThai();
    }

    public void save(LoaiGiay lg){
        loaiGiayRepository.save(lg);
    }


}
