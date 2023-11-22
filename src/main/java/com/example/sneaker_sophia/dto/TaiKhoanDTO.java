package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.VaiTro;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoanDTO {
    private String id;

    private VaiTro vaiTro;

    private String ten;

    private String email;

    private String matKhau;

    private String canCuoc;

    private String sdt;

    private LocalDate ngaySinh;

    private Integer gioiTinh;

    private String anhDaiDien;

    private Integer trangThai;



    public TaiKhoanDTO(TaiKhoanRequest nhanVienRequest) {
        this.setTen(nhanVienRequest.getTen());
        this.setEmail(nhanVienRequest.getEmail());
        this.setNgaySinh(nhanVienRequest.getNgaySinh());
        this.setGioiTinh(nhanVienRequest.getGioiTinh());
        this.setCanCuoc(nhanVienRequest.getCanCuoc());
        this.setSdt(nhanVienRequest.getSdt());
        this.setTrangThai(nhanVienRequest.getTrangThai());
        this.setAnhDaiDien(nhanVienRequest.getAnhDaiDien());
        this.setVaiTro(VaiTro.builder().id(nhanVienRequest.getIdVaiTro()).build());
    }
}
