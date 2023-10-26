package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.repository.HoaDonRepository;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("hoaDonService")
public class HoaDonService {

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;

    public Integer soHD(){
        return hoaDonRepository.soHD();
    }

    public List<HoaDon> getHoaDonByTrangThai(){
        return hoaDonRepository.getHoaDonByTrangThai();
    }
}
