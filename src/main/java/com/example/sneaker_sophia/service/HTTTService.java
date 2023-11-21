package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HinhThucThanhToan;
import com.example.sneaker_sophia.repository.HTTTRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("htttService")
public class HTTTService {

    @Resource(name = "htttRepository")
    HTTTRepository htttRepository;

    public void savehttt(HinhThucThanhToan hinhThucThanhToan){
        htttRepository.save(hinhThucThanhToan);
    }

    public HinhThucThanhToan getHTTTByIdhd(String idhd){
        return htttRepository.getHTTTByIdhd(idhd);
    }
}