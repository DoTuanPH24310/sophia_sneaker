package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import com.example.sneaker_sophia.repository.LoaiGiayRepository;
import com.example.sneaker_sophia.service.DeGiayService;
import com.example.sneaker_sophia.service.LoaiGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiGiayIplm implements LoaiGiayService {
    @Autowired
    LoaiGiayRepository loaiGiayRepository;
    @Override
    public List<LoaiGiay> getAll() {
        return loaiGiayRepository.findAll();
    }
}
