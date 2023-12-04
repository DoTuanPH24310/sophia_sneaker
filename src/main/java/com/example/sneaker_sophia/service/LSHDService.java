package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.LichSuHoaDon;
import com.example.sneaker_sophia.repository.LSHDRepository;
import com.example.sneaker_sophia.repository.LichSuHoaDonWebRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("lshdService")
public class LSHDService {

    @Resource(name = "lshdRepository")
    LSHDRepository lshdRepository;
    @Autowired
    LichSuHoaDonWebRepository hoaDonWebRepository;

    public void savelshd(LichSuHoaDon lichSuHoaDon){
        lshdRepository.save(lichSuHoaDon);
    }

    public void deletelshd(LichSuHoaDon lichSuHoaDon){
        lshdRepository.delete(lichSuHoaDon);
    }

    public List<LichSuHoaDon> getLSHDBYIdhd(String idhd){
        return lshdRepository.getLSHDBYIdhd(idhd);
    }
    //thong ke
    public int countByHoaDonTrangThaiAndPhuongThuc(LocalDateTime startDate, LocalDateTime endDate){
        return hoaDonWebRepository.countByHoaDonTrangThaiAndPhuongThuc(startDate,endDate);
    }
    public Double sumTongTienByHoaDonTrangThaiAndPhuongThuc(LocalDateTime startDate, LocalDateTime endDate){
        return hoaDonWebRepository.sumTongTienByHoaDonTrangThaiAndPhuongThuc(startDate,endDate);
    }
}