package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;
@Service
public class ChiTietGiayIplm implements ChiTietGiayService {
    @Autowired
    ChiTietGiayRepository chiTietGiayRepository;
    @Override
    public List<ChiTietGiay> getAll() {
        return chiTietGiayRepository.findAll();
    }

    @Override
    public void add(ChiTietGiay chiTietGiay) {
        chiTietGiayRepository.save(chiTietGiay);
    }

}
