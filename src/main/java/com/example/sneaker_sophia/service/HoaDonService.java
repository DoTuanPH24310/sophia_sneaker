package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DTO_API_HoaDon;
import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.repository.HoaDonRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@Service("hoaDonService")
public class HoaDonService {

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;



    @Autowired
    private HttpSession session;

    public Integer soHD() {
        return hoaDonRepository.soHD();
    }

    public List<HoaDon> getHoaDonByTrangThai() {
        return hoaDonRepository.getHoaDonByTrangThai();
    }

    public HoaDon addHD(Model model) {
        if (hoaDonRepository.countHoaDonByTrangThai() >= 5) {
            session.setAttribute("errTaiQuay", "Chỉ tạo được tối đa 5 hóa đơn");
            return null;
        } else {
            HoaDon hoaDon = new HoaDon();
            int soHD = hoaDonRepository.soHD() + 1;
            hoaDon.setMaHoaDOn("HD" + soHD);
            hoaDon.setTrangThai(2);
            hoaDon.setLoaiHoaDon(1);
            hoaDon.setPhiShip(0.0);
            hoaDon.setTongTien(0.0);
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

    // 18 - 11
    public List<HoaDon> getALl(){
        return hoaDonRepository.findAll();
    }

    // 19-11

    public List<HoaDon> getAllHDHT(){
        return hoaDonRepository.getAllHDHT();
    }

    public List<HoaDon> getAllHDC(){
        return hoaDonRepository.getAllHDC();
    }

    public List<HoaDon> getAllHDChoGiao(){
        return hoaDonRepository.getAllHDChoGiao();
    }

    public List<HoaDon> getAllHDChoXacNhan(){
        return hoaDonRepository.getAllHDCXN();
    }

    public List<HoaDon> getAllHDDangGiao(){
        return hoaDonRepository.getAllHDDangGiao();
    }

    public List<HoaDon> getAllHDHuy(){
        return hoaDonRepository.getAllHDHuy();
    }

    ///////////    20/11
    public List<HoaDon> findHoaDonByMultipleParamsAPI(DTO_API_HoaDon hd) {
        LocalDateTime ngayBatDau = hd.getNgayBatDau().equals("") ? null: LocalDateTime.parse(hd.getNgayBatDau());
        LocalDateTime ngayKetThuc = hd.getNgayKetThuc().equals("") || hd.getNgayBatDau().equals("") ? null: LocalDateTime.parse(hd.getNgayKetThuc());
        Integer loaiDon = hd.getLoaiDon().equals("") ? null: Integer.parseInt(hd.getLoaiDon());
        String txtSearch = hd.getTextSearch().equals("") ? null: "%"+hd.getTextSearch()+"%";
       if (hd.getSapXep().equals("1")){
           return hoaDonRepository.findHoaDonByMultipleParamsAPIDate(ngayBatDau,ngayKetThuc,Integer.parseInt(hd.getTrangThai()),loaiDon,txtSearch);
       }else if(hd.getSapXep().equals("2")){
           return hoaDonRepository.findHoaDonByMultipleParamsAPImaHoaDOn(ngayBatDau,ngayKetThuc,Integer.parseInt(hd.getTrangThai()),loaiDon,txtSearch);
       }
        return hoaDonRepository.findHoaDonByMultipleParamsAPITongTien(ngayBatDau,ngayKetThuc,Integer.parseInt(hd.getTrangThai()),loaiDon,txtSearch);
    }

    public Integer soHDCG(){
        return hoaDonRepository.soHDCG();
    }

    public Integer soHDCXN(){
        return hoaDonRepository.soHDCXN();
    }

    public Integer soHDDG(){
        return hoaDonRepository.soHDDG();
    }
}