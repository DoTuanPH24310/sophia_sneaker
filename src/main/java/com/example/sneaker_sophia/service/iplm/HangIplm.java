package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import com.example.sneaker_sophia.service.HangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HangIplm implements HangService {
    @Autowired
    HangRepository hangRepository;

    @Override
    public List<Hang> getAll() {
        return hangRepository.findAll();
    }
}
