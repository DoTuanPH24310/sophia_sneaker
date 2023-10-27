package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.repository.HoaDonCTRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("hoaDonChiTietServive")
public class HoaDonChiTietServive {
    @Resource(name = "hoaDonCTRepository")
    HoaDonCTRepository hoaDonCTRepository;

    public List<HoaDonChiTiet> getHDCTByIdHD(String idhd){
        return hoaDonCTRepository.getHDCTByIdHD(idhd);
    }

    public void deleteByIdCTSP(UUID idhdct){
        hoaDonCTRepository.deleteByIdCTSP(idhdct);
    }
}
