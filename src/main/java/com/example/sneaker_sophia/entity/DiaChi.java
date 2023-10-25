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
//@Table(name = "DiaChi")
//public class DiaChi {
//    @Id
//    @Column(name = "Id")
//    private UUID id;
//
//    @Column(name = "IdTaiKhoan")
//    private UUID idTaiKhoan;
//
//    @Column(name = "ten")
//    private String ten;
//
//    @Column(name = "sdt")
//    private String sdt;
//
//    @Column(name = "diaChiCuThe")
//    private String diaChiCuThe;
//
//    @Column(name = "phuongXa")
//    private String phuongXa;
//
//    @Column(name = "quanHuyen")
//    private String quanHuyen;
//
//    @Column(name = "tinh")
//    private String tinh;
//
//    @Column(name = "diaChiMacDinh")
//    private Integer diaChiMacDinh;
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
//    public UUID getIdTaiKhoan() {
//        return this.idTaiKhoan;
//    }
//
//    public void setIdTaiKhoan(UUID idTaiKhoan) {
//        this.idTaiKhoan = idTaiKhoan;
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
//    public String getSdt() {
//        return this.sdt;
//    }
//
//    public void setSdt(String sdt) {
//        this.sdt = sdt;
//    }
//
//    public String getDiaChiCuThe() {
//        return this.diaChiCuThe;
//    }
//
//    public void setDiaChiCuThe(String diaChiCuThe) {
//        this.diaChiCuThe = diaChiCuThe;
//    }
//
//    public String getPhuongXa() {
//        return this.phuongXa;
//    }
//
//    public void setPhuongXa(String phuongXa) {
//        this.phuongXa = phuongXa;
//    }
//
//    public String getQuanHuyen() {
//        return this.quanHuyen;
//    }
//
//    public void setQuanHuyen(String quanHuyen) {
//        this.quanHuyen = quanHuyen;
//    }
//
//    public String getTinh() {
//        return this.tinh;
//    }
//
//    public void setTinh(String tinh) {
//        this.tinh = tinh;
//    }
//
//    public Integer getDiaChiMacDinh() {
//        return this.diaChiMacDinh;
//    }
//
//    public void setDiaChiMacDinh(Integer diaChiMacDinh) {
//        this.diaChiMacDinh = diaChiMacDinh;
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