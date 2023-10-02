package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ChiTietGiay")
public class ChiTietGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdGiay")
    private Long idGiay;

    @Column(name = "IdKichCo")
    private Long idKichCo;

    @Column(name = "IdLoaiGiay")
    private Long idLoaiGiay;

    @Column(name = "IdHang")
    private Long idHang;

    @Column(name = "IdDeGiay")
    private Long idDeGiay;

    @Column(name = "IdMauSac")
    private Long idMauSac;

    @Column(name = "Code")
    private String code;

    @Column(name = "DonGia")
    private Long donGia;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdGiay() {
        return this.idGiay;
    }

    public void setIdGiay(Long idGiay) {
        this.idGiay = idGiay;
    }

    public Long getIdKichCo() {
        return this.idKichCo;
    }

    public void setIdKichCo(Long idKichCo) {
        this.idKichCo = idKichCo;
    }

    public Long getIdLoaiGiay() {
        return this.idLoaiGiay;
    }

    public void setIdLoaiGiay(Long idLoaiGiay) {
        this.idLoaiGiay = idLoaiGiay;
    }

    public Long getIdHang() {
        return this.idHang;
    }

    public void setIdHang(Long idHang) {
        this.idHang = idHang;
    }

    public Long getIdDeGiay() {
        return this.idDeGiay;
    }

    public void setIdDeGiay(Long idDeGiay) {
        this.idDeGiay = idDeGiay;
    }

    public Long getIdMauSac() {
        return this.idMauSac;
    }

    public void setIdMauSac(Long idMauSac) {
        this.idMauSac = idMauSac;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getDonGia() {
        return this.donGia;
    }

    public void setDonGia(Long donGia) {
        this.donGia = donGia;
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
