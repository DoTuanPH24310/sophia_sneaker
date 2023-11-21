package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("diaChiService")
public class DiaChiService {
    @Resource(name = "diaChiRepository")
    DiaChiRepository diaChiRepository;

    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    public  DiaChi getDiaChiByIdTaiKhoan(String id){
        return  diaChiRepository.getDiaChiByIdTaiKhoan(id);
    }

    public DiaChi getNhanVienDTOById(String id){
        return diaChiRepository.getNhanVienDTOById(id);
    }

    // 15-11
    public List<DiaChi> getAllDCByIdkh(String idkh){
        return diaChiRepository.findListDCByIdKH(idkh);
    }

    public void updateDCMD(String iddc, HttpSession session){
        String idkh = (String) session.getAttribute("idkh");
        DiaChi diaChiThuong = diaChiRepository.findById(iddc).orElse(null);
        DiaChi diaChiMD = diaChiRepository.getDiaChiByIdTaiKhoan(idkh);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiRepository.save(diaChiMD);
        }
        if (diaChiThuong.getDiaChiMacDinh() == 0) {
            diaChiThuong.setDiaChiMacDinh(1);
            diaChiRepository.save(diaChiThuong);
        }
    }

    public void adddc(Integer xa, Integer quan, Integer tinh, String dcCuThe, String hoTen, String sdt, HttpSession session){
        String idkh = (String) session.getAttribute("idkh");
        List<DiaChi> listDC = diaChiRepository.findListDCByIdKH(idkh);
        DiaChi diaChi = new DiaChi();
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByIdKH(idkh);
        DiaChi diaChiMD = diaChiRepository.getDiaChiByIdTaiKhoan(idkh);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiRepository.save(diaChiMD);
        }
        diaChi.setTaiKhoan(taiKhoan);
        diaChi.setPhuongXa(xa);
        diaChi.setQuanHuyen(quan);
        diaChi.setTinh(tinh);
        diaChi.setDiaChiCuThe(dcCuThe);
        diaChi.setTen(hoTen);
        diaChi.setSdt(sdt);
        diaChi.setDiaChiMacDinh(1);
        diaChiRepository.save(diaChi);
    }

    public void deleteById(String id) {
        diaChiRepository.deleteById(id);
    }


    // cuongdv


    // 31-10

    public DiaChi getDiaChiById(String iddc) {
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public void saveDC(DiaChi diaChi) {
        diaChiRepository.save(diaChi);
    }

    public List<DiaChi> findListTKById(String idkh) {
        return diaChiRepository.findListTKByIdKH(idkh);
    }

    // 3-11

    public DiaChi findDcByIdDc(String iddc) {
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public DiaChi findListTKByIdKHAndDCMD(String idkh) {
        return diaChiRepository.findListTKByIdKHAndDCMD(idkh);
    }


    //    14/11
    public Integer getCountDiaChi(String idTaiKhoan) {
        return diaChiRepository.getCountDiaChi(UUID.fromString(idTaiKhoan));
    }

}
