package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HinhThucThanhToan")
public class HinhThucThanhToan {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdHoaDon")
    private String idHoaDon;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "soTien")
    private String soTien;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
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

    public String getSoTien() {
        return this.soTien;
    }

    public void setSoTien(String soTien) {
        this.soTien = soTien;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
