package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonService {
    @Autowired
    private HoaDonWebRepository hoaDonRepository;

    public List<Object[]> getDoanhThuTheoThang(int nam) {
        return hoaDonRepository.getDoanhThuTheoThang(nam);
    }
}
