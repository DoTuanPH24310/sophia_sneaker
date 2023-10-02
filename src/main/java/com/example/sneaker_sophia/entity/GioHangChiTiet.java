package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @Column(name = "IdGioHang")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idGioHang;

    @Id
    @Column(name = "IdChiTietGiay")
    private String idChiTietGiay;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "ngayTao")
    private String ngayTao;

    @Column(name = "ngaySua")
    private String ngaySua;

    public String getIdGioHang() {
        return this.idGioHang;
    }

    public void setIdGioHang(String idGioHang) {
        this.idGioHang = idGioHang;
    }

    public String getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(String idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgaySua() {
        return this.ngaySua;
    }

    public void setNgaySua(String ngaySua) {
        this.ngaySua = ngaySua;
    }
}
