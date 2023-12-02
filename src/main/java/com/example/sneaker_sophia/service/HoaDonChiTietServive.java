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

    public void deleteHDCT(UUID idctsp, String idhd) {
        HoaDonChiTiet hdct = hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if (hdct != null) {
            if (hdct.getSoLuongGiam() > 0) {
                List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
                List<Voucher> voucherListHH = kmService.getAllKMByIdctgHH(chiTietGiay.getId());
                for (Voucher voucher : voucherList) {
//                    if(hdct.getCreatedDate().compareTo(voucher.getNgayBatDau()) > 0 || hdct.getCreatedDate().compareTo(voucher.getNgayKetThuc()) < 0){
                    for (int i = 0; i < hdct.getSoLuongGiam(); i++) {
                        if (voucher.getSoLuong() < voucher.getSoLuongGiam()) {
                            voucher.setSoLuong(voucher.getSoLuong() + 1);
                            kmService.saveVC(voucher);
                        } else {
                            for (Voucher voucherhh : voucherListHH) {
                                if (hdct.getCreatedDate().compareTo(voucherhh.getNgayBatDau()) > 0 || hdct.getCreatedDate().compareTo(voucherhh.getNgayKetThuc()) < 0) {
                                    voucherhh.setSoLuong(voucherhh.getSoLuong() + 1);
                                    kmService.saveVC(voucherhh);
                                }

                            }
                        }
                    }

//                    }
                }
//                for (Voucher voucher : voucherListHH) {
//                    if (hdct.getCreatedDate().compareTo(voucher.getNgayBatDau()) > 0 || hdct.getCreatedDate().compareTo(voucher.getNgayKetThuc()) < 0) {
//                        voucher.setSoLuong(hdct.getSoLuongGiam() + voucher.getSoLuong());
//                        kmService.saveVC(voucher);
//                    }
//
//                }
            }


            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + hdct.getSoLuong());
            chiTietGiayService.save(chiTietGiay);
            hoaDonCTRepository.delete(hdct);
        }
    }

    public void updateDownSLHDCT(UUID idctsp, String idhd, Model model) {
        HoaDonChiTiet hdct = hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if (hdct.getSoLuong() == 1) {
            hdct.setSoLuong(1);
            session.setAttribute("errTaiQuay", "Số lượng tối thiểu là 1");
        } else {
            List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
            List<Voucher> voucherListHH = kmService.getAllKMByIdctgHH(chiTietGiay.getId());
            Voucher voucherHD = kmService.getKMByIdctg(chiTietGiay.getId());
           if(voucherHD != null) {
                if (voucherHD.getSoLuong() < voucherHD.getSoLuongGiam()) {
                    if (hdct.getSoLuong() <= hdct.getSoLuongGiam()) {
                        voucherHD.setSoLuong(voucherHD.getSoLuong() + 1);
                        hdct.setPhanTramGiam(hdct.getPhanTramGiam() - voucherHD.getPhanTramGiam());
                        hdct.setSoLuongGiam(hdct.getSoLuongGiam() - 1);
                        kmService.saveVC(voucherHD);
                    }
                } else {
                    for (Voucher voucherhh : voucherListHH) {
                        if (hdct.getCreatedDate().compareTo(voucherhh.getNgayBatDau()) > 0 || hdct.getCreatedDate().compareTo(voucherhh.getNgayKetThuc()) < 0) {
                            voucherhh.setSoLuong(voucherhh.getSoLuong() + 1);
                            hdct.setPhanTramGiam(hdct.getPhanTramGiam() - voucherhh.getPhanTramGiam());
                            hdct.setSoLuongGiam(hdct.getSoLuongGiam() - 1);
                            kmService.saveVC(voucherhh);
                        }

                    }
                }

            }
            hdct.setSoLuong(hdct.getSoLuong() - 1);
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() + 1);
        }

        chiTietGiayService.save(chiTietGiay);
        hoaDonCTRepository.save(hdct);
    }

    public void updateUpSLHDCT(UUID idctsp, String idhd, Model model) {
        HoaDonChiTiet hdct = hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        if (hdct.getSoLuong() >= hdct.getSoLuong() + chiTietGiay.getSoLuong()) {
            hdct.setSoLuong(hdct.getSoLuong());
            session.setAttribute("errTaiQuay", "Không đủ số lượng tồn");
        } else {
            if (chiTietGiayService.tongKMByIdctg(chiTietGiay.getId()) != null) {
                List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
                for (Voucher voucher : voucherList) {
                    if (voucher.getSoLuong() > 0) {
                        voucher.setSoLuong(voucher.getSoLuong() - 1);
                        hdct.setSoLuongGiam(hdct.getSoLuongGiam() + 1);
                        hdct.setPhanTramGiam(hdct.getPhanTramGiam() + voucher.getPhanTramGiam());
                        kmService.saveVC(voucher);

                    } else {
                        hdct.setSoLuongGiam(hdct.getSoLuongGiam());
                    }
                }
            }
            hdct.setSoLuong(hdct.getSoLuong() + 1);
            chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - 1);
        }

        chiTietGiayService.save(chiTietGiay);
        hoaDonCTRepository.save(hdct);
    }

    //    //28-10 cuongdv

    public void addhdct(HoaDonChiTiet hoaDonChiTiet) {
        hoaDonCTRepository.save(hoaDonChiTiet);
    }

    public HoaDonChiTiet getHDCTByIdCTSP(UUID idctsp, String idhd) {
        return hoaDonCTRepository.getHDCTByIdCTSP(idctsp, idhd);
    }

    public Double tongTienSauGiam(String idhd) {
        return hoaDonCTRepository.tongTienSauGiam(idhd);
    }

    public Double tongTienTruocGiam(String idhd) {
        return hoaDonCTRepository.tongTienTruocGiam(idhd);
    }

    public Double tienGiam(String idhd) {
        return hoaDonCTRepository.tienGiam(idhd);
    }

    public List<Object[]> findTop10IdChiTietGiay(){
        return hoaDonCTRepository.findTop10IdChiTietGiay();
    }

    //thong ke
    public Integer sumSoLuongByHoaDonTrangThaiEquals1(){
        return hoaDonCTRepository.sumSoLuongByHoaDonTrangThaiEquals1();
    }
}