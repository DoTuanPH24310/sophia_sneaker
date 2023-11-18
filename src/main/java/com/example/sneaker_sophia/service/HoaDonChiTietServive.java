package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.HoaDonCTRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private HttpSession session;

    @Resource(name = "kmService")
    KMService kmService;

    public List<HoaDonChiTiet> getHDCTByIdHD(String idhd){
        return hoaDonCTRepository.getHDCTByIdHD(idhd);
    }

    public void deleteHDCT(UUID idctsp, String idhd){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);

        if (hdct != null){
            if(hdct.getSoLuongGiam() >0){
                List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
                for (Voucher voucher : voucherList) {
                    voucher.setSoLuong(hdct.getSoLuongGiam()+voucher.getSoLuong());
                    kmService.saveVC(voucher);
                }
            }

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
            session.setAttribute("errTaiQuay", "Số lượng tối thiểu là 1");
        }else{
            List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
            for(Voucher voucher : voucherList) {
                if(hdct.getSoLuong() <= hdct.getSoLuongGiam()){
                    voucher.setSoLuong(voucher.getSoLuong() + 1);
                    hdct.setSoLuongGiam(hdct.getSoLuongGiam() - 1);
                }
            }
            hdct.setSoLuong(hdct.getSoLuong() - 1);
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + 1);
        }

        chiTietGiayService.save(chiTietGiay);
        hoaDonCTRepository.save(hdct);
    }

    public void updateUpSLHDCT(UUID idctsp, String idhd, Model model){
        HoaDonChiTiet hdct =  hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if(hdct.getSoLuong() >= hdct.getSoLuong() + chiTietGiay.getSoLuong()){
            hdct.setSoLuong(hdct.getSoLuong());
            session.setAttribute("errTaiQuay", "Không đủ số lượng tồn");
        }else{
            if (chiTietGiayService.tongKMByIdctg(chiTietGiay.getId()) != null) {
                List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
                for(Voucher voucher : voucherList){
                    if (voucher.getSoLuong() > 0){
                        hdct.setSoLuongGiam(hdct.getSoLuongGiam() + 1);
                        voucher.setSoLuong(voucher.getSoLuong() - 1);
                        kmService.saveVC(voucher);

                    }else{
                        hdct.setSoLuongGiam(hdct.getSoLuongGiam());
                    }
                    hoaDonCTRepository.save(hdct);
                }
            }
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
    public Double tongTienHD(String idhd){
        return hoaDonCTRepository.tongTienHD(idhd);
    }

}