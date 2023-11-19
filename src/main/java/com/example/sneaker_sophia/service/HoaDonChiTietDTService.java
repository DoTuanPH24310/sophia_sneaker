package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.repository.HoaDonChiTietWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietDTService {

    @Autowired
    HoaDonChiTietWebRepository hoaDonChiTietRepository;

    public List<Object[]> findTop10BestSellingProductsByNam(int year) {
        return hoaDonChiTietRepository.findTop10BestSellingProductsByNam(year);
    }

    public List<HoaDonChiTiet> findAll(){
        return hoaDonChiTietRepository.findAll();
    }
}
