package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.TaiKhoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NhanVienDTO {
    private String id;

    private String ten;

    private String email;

    private String anhDaiDien;

    private String canCuoc;

    private LocalDate ngaySinh;

    private String sdt;

    private Integer gioiTinh;

    private Integer trangThai;

    private Integer tinh ;

    private Integer quanHuyen;

    private Integer phuongXa;

    public NhanVienDTO(TaiKhoan taiKhoan) {
        this.setId(taiKhoan.getId());
        this.setTen(taiKhoan.getTen());
        this.setAnhDaiDien(taiKhoan.getAnhDaiDien());
        this.setEmail(taiKhoan.getEmail());
        this.setCanCuoc(taiKhoan.getCanCuoc());
        this.setNgaySinh(taiKhoan.getNgaySinh());
        this.setSdt(taiKhoan.getSdt());
        this.setGioiTinh(taiKhoan.getGioiTinh());
        this.setTrangThai(taiKhoan.getTrangThai());
    }

}
