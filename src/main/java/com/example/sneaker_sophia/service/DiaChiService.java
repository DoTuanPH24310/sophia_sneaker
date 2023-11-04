package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("diaChiService")
public class DiaChiService {
    @Resource(name = "diaChiRepository")
    DiaChiRepository diaChiRepository;

    public  DiaChi getDiaChiByIdTaiKhoan(String id){
        return  diaChiRepository.getDiaChiByIdTaiKhoan(id);
    }

    public DiaChi getNhanVienDTOById(String id){
        return diaChiRepository.getNhanVienDTOById(id);
    }

    // 31-10

    public DiaChi getDiaChiById(String iddc){
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public void saveDC(DiaChi diaChi){
        diaChiRepository.save(diaChi);
    }

    public List<DiaChi> findListTKById(String idkh){
        return diaChiRepository.findListTKByIdKH(idkh);
    }

    // 3-11

    public DiaChi findDcByIdDc(String iddc){
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public DiaChi findListTKByIdKHAndDCMD(String idkh){
        return diaChiRepository.findListTKByIdKHAndDCMD(idkh);
    }
}