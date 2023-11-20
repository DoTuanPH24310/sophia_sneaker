package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.HoaDon;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class VNPayDTO {
    private UUID id;
    private double tienChuyenKhoan;
    private String nganHang;
    private String maGiaoDich;
    private LocalDate ngayTao;
    private LocalDate ngaySua;
    private String nguoiTao;
    private String nguoiSua;
    private Integer trangThai;
    private HoaDon hoaDon;
}
