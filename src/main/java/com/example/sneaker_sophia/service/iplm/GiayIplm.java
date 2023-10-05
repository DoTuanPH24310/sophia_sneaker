package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.GiayRepository;
import com.example.sneaker_sophia.service.GiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiayIplm implements GiayService {
    @Autowired
    GiayRepository giayRepository;
    @Override
    public List<Giay> getAll() {
        return giayRepository.findAll();
    }
}
