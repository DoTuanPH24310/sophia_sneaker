package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.repository.KichCoRepository;
import com.example.sneaker_sophia.service.KichCoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KichCoIplm implements KichCoService {
    @Autowired
    KichCoRepository kichCoRepository;
    @Override
    public List<KichCo> getAll() {
        return kichCoRepository.findAll();
    }
}
