package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.repository.HoaDonRepository;
import jakarta.annotation.Resource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service("hoaDonService")
public class HoaDonService {

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;

    public Integer soHD() {
        return hoaDonRepository.soHD();
    }

    public List<HoaDon> getHoaDonByTrangThai() {
        return hoaDonRepository.getHoaDonByTrangThai();
    }

    public HoaDon addHD(Model model) {
        if (hoaDonRepository.countHoaDonByTrangThai() >= 5) {
            model.addAttribute("errAddHd", "Không thể thêm mới hóa đơn");
            return null;
        } else {
            HoaDon hoaDon = new HoaDon();
            int soHD = hoaDonRepository.soHD() + 1;
            hoaDon.setMaHoaDOn("HD" + soHD);
            hoaDon.setTrangThai(2);
            hoaDon.setLoaiHoaDon(1);
            return hoaDonRepository.save(hoaDon);
        }

    }

    public HoaDon getHoaDonById(String idhd) {
        return hoaDonRepository.findById(idhd).orElse(null);
    }

    public void deleteHD(HoaDon hoaDon) {
        hoaDonRepository.delete(hoaDon);
    }

    //28-10
    public HoaDon findHDById(String idhd) {
        return hoaDonRepository.findById(idhd).orElse(null);
    }

    // 30/10 cuongdv

    public void savehd(HoaDon hoaDon){
        hoaDonRepository.save(hoaDon);
    }
}