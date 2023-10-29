package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaiKhoanDiaChi {
    private TaiKhoan taiKhoan;

    private DiaChi diaChi;

//    public TaiKhoanDiaChi() {
//    }
}