//package com.example.sneaker_sophia.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import java.util.UUID;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "ChiTietGiay")
//public class ChiTietGiay {
//    @Id
//    @Column(name = "Id")
//    private UUID id;
//
//    @Column(name = "IdGiay")
//    private UUID idGiay;
//
//    @Column(name = "IdKichCo")
//    private UUID idKichCo;
//
//    @Column(name = "IdDeGiay")
//    private UUID idDeGiay;
//
//    @Column(name = "IdHang")
//    private UUID idHang;
//
//    @Column(name = "IdLoaiGiay")
//    private UUID idLoaiGiay;
//
//    @Column(name = "IdMauSac")
//    private UUID idMauSac;
//
//    @Column(name = "ma")
//    private String ma;
//
//    @Column(name = "gia")
//    private UUID gia;
//
//    @Column(name = "soLuong")
//    private Integer soLuong;
//
//    @Column(name = "ngayTao")
//    private UUID ngayTao;
//
//    @Column(name = "ngaySua")
//    private UUID ngaySua;
//
//    @Column(name = "nguoiTao")
//    private UUID nguoiTao;
//
//    @Column(name = "nguoiSua")
//    private UUID nguoiSua;
//
//    @Column(name = "trangThai")
//    private Integer trangThai;
//
//    public UUID getId() {
//        return this.id;
//    }
//
//    public void setId(UUID id) {
//        this.id = id;
//    }
//
//    public UUID getIdGiay() {
//        return this.idGiay;
//    }
//
//    public void setIdGiay(UUID idGiay) {
//        this.idGiay = idGiay;
//    }
//
//    public UUID getIdKichCo() {
//        return this.idKichCo;
//    }
//
//    public void setIdKichCo(UUID idKichCo) {
//        this.idKichCo = idKichCo;
//    }
//
//    public UUID getIdDeGiay() {
//        return this.idDeGiay;
//    }
//
//    public void setIdDeGiay(UUID idDeGiay) {
//        this.idDeGiay = idDeGiay;
//    }
//
//    public UUID getIdHang() {
//        return this.idHang;
//    }
//
//    public void setIdHang(UUID idHang) {
//        this.idHang = idHang;
//    }
//
//    public UUID getIdLoaiGiay() {
//        return this.idLoaiGiay;
//    }
//
//    public void setIdLoaiGiay(UUID idLoaiGiay) {
//        this.idLoaiGiay = idLoaiGiay;
//    }
//
//    public UUID getIdMauSac() {
//        return this.idMauSac;
//    }
//
//    public void setIdMauSac(UUID idMauSac) {
//        this.idMauSac = idMauSac;
//    }
//
//    public String getMa() {
//        return this.ma;
//    }
//
//    public void setMa(String ma) {
//        this.ma = ma;
//    }
//
//    public UUID getGia() {
//        return this.gia;
//    }
//
//    public void setGia(UUID gia) {
//        this.gia = gia;
//    }
//
//    public Integer getSoLuong() {
//        return this.soLuong;
//    }
//
//    public void setSoLuong(Integer soLuong) {
//        this.soLuong = soLuong;
//    }
//
//    public UUID getNgayTao() {
//        return this.ngayTao;
//    }
//
//    public void setNgayTao(UUID ngayTao) {
//        this.ngayTao = ngayTao;
//    }
//
//    public UUID getNgaySua() {
//        return this.ngaySua;
//    }
//
//    public void setNgaySua(UUID ngaySua) {
//        this.ngaySua = ngaySua;
//    }
//
//    public UUID getNguoiTao() {
//        return this.nguoiTao;
//    }
//
//    public void setNguoiTao(UUID nguoiTao) {
//        this.nguoiTao = nguoiTao;
//    }
//
//    public UUID getNguoiSua() {
//        return this.nguoiSua;
//    }
//
//    public void setNguoiSua(UUID nguoiSua) {
//        this.nguoiSua = nguoiSua;
//    }
//
//    public Integer getTrangThai() {
//        return this.trangThai;
//    }
//
//    public void setTrangThai(Integer trangThai) {
//        this.trangThai = trangThai;
//    }
//}
