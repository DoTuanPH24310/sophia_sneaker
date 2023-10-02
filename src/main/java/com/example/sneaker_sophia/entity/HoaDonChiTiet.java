package com.example.sneaker_sophia.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import jakarta.persistence.*;

@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdChiTietGiay")
    private String idChiTietGiay;

    @Column(name = "IdHoaDon")
    private String idHoaDon;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "donGia")
    private String donGia;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(String idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public String getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public String getDonGia() {
        return this.donGia;
    }

    public void setDonGia(String donGia) {
        this.donGia = donGia;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
