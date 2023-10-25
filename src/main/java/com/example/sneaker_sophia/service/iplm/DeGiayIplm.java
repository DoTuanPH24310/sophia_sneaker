package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.DeGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeGiayIplm implements DeGiayService {
    @Autowired
    DeGiayRepository deGiayRepository;
    @Override
    public List<DeGiay> getAll() {
        return deGiayRepository.findAll();
    }
}
