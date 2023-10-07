package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @Column(name = "IdGioHang")
    private UUID idGioHang;

    @Id
    @Column(name = "IdChiTietGiay")
    private UUID idChiTietGiay;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "ngayTao")
    private UUID ngayTao;

    @Column(name = "ngaySua")
    private UUID ngaySua;

    @Column(name = "nguoiTao")
    private UUID nguoiTao;

    @Column(name = "nguoiSua")
    private UUID nguoiSua;

    public UUID getIdGioHang() {
        return this.idGioHang;
    }

    public void setIdGioHang(UUID idGioHang) {
        this.idGioHang = idGioHang;
    }

    public UUID getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(UUID idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public UUID getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(UUID ngayTao) {
        this.ngayTao = ngayTao;
    }

    public UUID getNgaySua() {
        return this.ngaySua;
    }

    public void setNgaySua(UUID ngaySua) {
        this.ngaySua = ngaySua;
    }

    public UUID getNguoiTao() {
        return this.nguoiTao;
    }

    public void setNguoiTao(UUID nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public UUID getNguoiSua() {
        return this.nguoiSua;
    }

    public void setNguoiSua(UUID nguoiSua) {
        this.nguoiSua = nguoiSua;
    }
}
