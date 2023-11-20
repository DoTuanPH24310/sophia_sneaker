package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.LichSuHoaDon;
import com.example.sneaker_sophia.repository.LSHDRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("lshdService")
public class LSHDService {

    @Resource(name = "lshdRepository")
    LSHDRepository lshdRepository;

    public void savelshd(LichSuHoaDon lichSuHoaDon){
        lshdRepository.save(lichSuHoaDon);
    }
}
