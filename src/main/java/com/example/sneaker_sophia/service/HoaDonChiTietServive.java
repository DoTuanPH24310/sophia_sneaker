package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.repository.HoaDonCTRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;

@Service("hoaDonChiTietServive")
public class HoaDonChiTietServive {
    @Resource(name = "hoaDonCTRepository")
    HoaDonCTRepository hoaDonCTRepository;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    public List<HoaDonChiTiet> getHDCTByIdHD(String idhd){
        return hoaDonCTRepository.getHDCTByIdHD(idhd);
    }

    public void deleteHDCT(UUID idctsp, String idhd){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if (hdct != null){
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + hdct.getSoLuong());
            chiTietGiayService.save(chiTietGiay);
            hoaDonCTRepository.delete(hdct);
        }
    }

    public void updateDownSLHDCT(UUID idctsp, String idhd, Model model){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if (hdct.getSoLuong() == 1){
            hdct.setSoLuong(1);
            model.addAttribute("errupdateDownSLHDCT", "Không thể giảm thêm nữa");
        }else{
            hdct.setSoLuong(hdct.getSoLuong() - 1);
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + 1);
        }

        chiTietGiayService.save(chiTietGiay);
        hoaDonCTRepository.save(hdct);
    }

    public void updateUpSLHDCT(UUID idctsp, String idhd, Model model){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if(hdct.getSoLuong() >= hdct.getIdHoaDonCT().getChiTietGiay().getSoLuong()){
            hdct.setSoLuong(hdct.getSoLuong());
            model.addAttribute("errupdateUpSLHDCT", "Không thể tăng thêm nữa");
        }else{
            hdct.setSoLuong(hdct.getSoLuong() + 1);
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - 1);
        }
        chiTietGiayService.save(chiTietGiay);
        hoaDonCTRepository.save(hdct);
    }

    //    //28-10 cuongdv

    public void addhdct(HoaDonChiTiet hoaDonChiTiet){
        hoaDonCTRepository.save(hoaDonChiTiet);
    }

    public HoaDonChiTiet getHDCTByIdCTSP(UUID idctsp, String idhd){
        return hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
    }
}