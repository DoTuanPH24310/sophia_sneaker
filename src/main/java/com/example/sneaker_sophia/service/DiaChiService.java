package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
