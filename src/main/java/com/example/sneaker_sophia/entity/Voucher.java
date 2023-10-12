package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Voucher")
public class Voucher {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "phanTramGiam")
    private String phanTramGiam;

    @Column(name = "soTienGiam")
    private String soTienGiam;

    @Column(name = "giaTriToiThieu")
    private String giaTriToiThieu;

    @Column(name = "ngayBatDau")
    private String ngayBatDau;

    @Column(name = "ngayKetThuc")
    private String ngayKetThuc;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMa() {
        return this.ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getPhanTramGiam() {
        return this.phanTramGiam;
    }

    public void setPhanTramGiam(String phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public String getSoTienGiam() {
        return this.soTienGiam;
    }

    public void setSoTienGiam(String soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public String getGiaTriToiThieu() {
        return this.giaTriToiThieu;
    }

    public void setGiaTriToiThieu(String giaTriToiThieu) {
        this.giaTriToiThieu = giaTriToiThieu;
    }

    public String getNgayBatDau() {
        return this.ngayBatDau;
    }

    public void setNgayBatDau(String ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public String getNgayKetThuc() {
        return this.ngayKetThuc;
    }

    public void setNgayKetThuc(String ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
