package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.Anh;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.service.AnhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AnhIplm implements AnhService {
    @Autowired
    AnhRepository anhRepository;

    @Override
    public List<Anh> getAll() {
        return anhRepository.findAll();
    }

    @Override
    public void save(Anh anh) {
        anhRepository.save(anh);
    }

    @Override
    public Anh getOne(UUID id) {
        return anhRepository.findById(id).get();
    }

    @Override
    public void delete(UUID id) {
        anhRepository.deleteById(id);
    }
}
