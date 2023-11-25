package com.example.sneaker_sophia.request;

import com.example.sneaker_sophia.dto.TaiKhoanDiaChi;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanRequest {

    private  String idTaiKhoan;

    private String idVaiTro;

    private String ten;

    private String anhDaiDien;

    private String email;

    private String canCuoc;

    private String ngaySinh;

    private String sdt;

    private String gioiTinh;

    private Integer trangThai;

    private Integer tinh;

    private Integer quanHuyen;

    private Integer phuongXa;

    private String diaChiCuThe;

    private String ghiChu;

    public TaiKhoanRequest(TaiKhoanDiaChi taiKhoanDiaChi) {
        this.setIdTaiKhoan(taiKhoanDiaChi.getTaiKhoan().getId());
        this.setTen(taiKhoanDiaChi.getTaiKhoan().getTen());
        this.setAnhDaiDien(taiKhoanDiaChi.getTaiKhoan().getAnhDaiDien());
        this.setEmail(taiKhoanDiaChi.getTaiKhoan().getEmail());
        this.setCanCuoc(taiKhoanDiaChi.getTaiKhoan().getCanCuoc());
        this.setNgaySinh(String.valueOf(taiKhoanDiaChi.getTaiKhoan().getNgaySinh()));
        this.setSdt(taiKhoanDiaChi.getTaiKhoan().getSdt());
        this.setGioiTinh(String.valueOf(taiKhoanDiaChi.getTaiKhoan().getGioiTinh()));
        this.setTrangThai(taiKhoanDiaChi.getTaiKhoan().getTrangThai());
        this.setTinh(taiKhoanDiaChi.getDiaChi().getTinh());
        this.setQuanHuyen(taiKhoanDiaChi.getDiaChi().getQuanHuyen());
        this.setPhuongXa(taiKhoanDiaChi.getDiaChi().getPhuongXa());
        this.setDiaChiCuThe(taiKhoanDiaChi.getDiaChi().getDiaChiCuThe());
    }
}