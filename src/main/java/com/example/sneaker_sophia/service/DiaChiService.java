package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

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
}