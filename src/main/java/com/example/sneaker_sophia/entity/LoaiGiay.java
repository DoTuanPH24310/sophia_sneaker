//package com.example.sneaker_sophia.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//import java.util.UUID;
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "LoaiGiay")
//public class LoaiGiay {
//    @Id
//    @Column(name = "Id")
//    private UUID id;
//
//    @Column(name = "ma")
//    private String ma;
//
//    @Column(name = "ten")
//    private String ten;
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
//    public String getMa() {
//        return this.ma;
//    }
//
//    public void setMa(String ma) {
//        this.ma = ma;
//    }
//
//    public String getTen() {
//        return this.ten;
//    }
//
//    public void setTen(String ten) {
//        this.ten = ten;
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
