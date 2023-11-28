package com.example.sneaker_sophia.service;

import org.springframework.stereotype.Service;


@Service("taiQuayService")
public class TaiQuayService {

    public boolean validateAđhttt(String xa, String quan, String tinh) {
        int dem = 0;
//        String errHoTen = null, errSDT = null, errDCCT = null, errTinh = null, errQuan = null, errPhuong = null;
        if(xa.length() > 50 || quan.length() > 50 || tinh.length() > 50
        ){
            dem++;
        }
        if (dem > 0){
            return false;
        }
        return dem == 0;
    }


}