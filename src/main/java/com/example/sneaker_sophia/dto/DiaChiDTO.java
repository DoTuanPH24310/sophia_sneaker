package com.example.sneaker_sophia.dto;


import com.example.sneaker_sophia.entity.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class DiaChiDTO {
    private String id;

    private String ten;

    private String sdt;

    private String diaChiCuThe;

    private Integer phuongXa;

    private Integer quanHuyen;

    private Integer tinh;

    private Integer diaChiMacDinh;

    private Integer trangThai;

    private TaiKhoan taiKhoan;
}
