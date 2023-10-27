package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.repository.HoaDonCTRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Service("hoaDonChiTietServive")
public class HoaDonChiTietServive {
    @Resource(name = "hoaDonCTRepository")
    HoaDonCTRepository hoaDonCTRepository;

    public List<HoaDonChiTiet> getHDCTByIdHD(String idhd){
        return hoaDonCTRepository.getHDCTByIdHD(idhd);
    }

    public void deleteHDCT(UUID idctsp){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp);

        hoaDonCTRepository.delete(hdct);
    }

    public void updateDownSLHDCT(UUID idctsp, Model model){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp);
        if (hdct.getSoLuong() == 1){
            hdct.setSoLuong(1);
            model.addAttribute("errupdateDownSLHDCT", "Không thể giảm thêm nữa");
        }else{
            hdct.setSoLuong(hdct.getSoLuong() - 1);
        }

        hoaDonCTRepository.save(hdct);
    }

    public void updateUpSLHDCT(UUID idctsp, Model model){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp);
        if(hdct.getSoLuong() >= hdct.getIdHoaDonCT().getChiTietGiay().getSoLuong()){
            hdct.setSoLuong(hdct.getSoLuong());
            model.addAttribute("errupdateUpSLHDCT", "Không thể tăng thêm nữa");
        }else{
            hdct.setSoLuong(hdct.getSoLuong() + 1);
        }

        hoaDonCTRepository.save(hdct);
    }
}