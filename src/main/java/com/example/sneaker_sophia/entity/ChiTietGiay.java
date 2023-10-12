package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietGiay")
public class ChiTietGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdGiay")
    private String idGiay;

    @Column(name = "IdKichCo")
    private String idKichCo;

    @Column(name = "IdDeGiay")
    private String idDeGiay;

    @Column(name = "IdHang")
    private String idHang;

    @Column(name = "IdLoaiGiay")
    private String idLoaiGiay;

    @Column(name = "IdMauSac")
    private String idMauSac;

    @Column(name = "ma")
    private String ma;

    @Column(name = "gia")
    private String gia;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGiay() {
        return this.idGiay;
    }

    public void setIdGiay(String idGiay) {
        this.idGiay = idGiay;
    }

    public String getIdKichCo() {
        return this.idKichCo;
    }

    public void setIdKichCo(String idKichCo) {
        this.idKichCo = idKichCo;
    }

    public String getIdDeGiay() {
        return this.idDeGiay;
    }

    public void setIdDeGiay(String idDeGiay) {
        this.idDeGiay = idDeGiay;
    }

    public String getIdHang() {
        return this.idHang;
    }

    public void setIdHang(String idHang) {
        this.idHang = idHang;
    }

    public String getIdLoaiGiay() {
        return this.idLoaiGiay;
    }

    public void setIdLoaiGiay(String idLoaiGiay) {
        this.idLoaiGiay = idLoaiGiay;
    }

    public String getIdMauSac() {
        return this.idMauSac;
    }

    public void setIdMauSac(String idMauSac) {
        this.idMauSac = idMauSac;
    }

    public String getMa() {
        return this.ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getGia() {
        return this.gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
