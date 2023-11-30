package com.example.sneaker_sophia.service;


import com.example.sneaker_sophia.entity.CTG_KhuyenMai;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.CTG_KhuyenMaiRespsitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CTG_KhuyenMaiService {

    @Autowired
    private CTG_KhuyenMaiRespsitory ctg_khuyenMaiRespsitory;

    public CTG_KhuyenMai save(CTG_KhuyenMai ctg_khuyenMai) {
        return ctg_khuyenMaiRespsitory.save(ctg_khuyenMai);
    }

    public Integer sumPhanTram(UUID id) {
        return ctg_khuyenMaiRespsitory.sumPhanTram(id);
    }

    public void deleteByIdKM(Voucher vc) {
         ctg_khuyenMaiRespsitory.deleteByIdKM(vc);
    }

    public List<String> findIdCTG(Voucher vc){
        return ctg_khuyenMaiRespsitory.findIdCTG(vc);
    }

    public List<ChiTietGiay> findCTG(Voucher vc){
        return ctg_khuyenMaiRespsitory.findCTG(vc);
    }
}
