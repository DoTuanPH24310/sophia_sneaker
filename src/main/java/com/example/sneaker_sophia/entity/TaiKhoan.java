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
//@Table(name = "TaiKhoan")
//public class TaiKhoan {
//    @Id
//    @Column(name = "Id")
//    private UUID id;
//
//    @Column(name = "IdVaiTro")
//    private UUID idVaiTro;
//
//    @Column(name = "taiKhoan")
//    private String taiKhoan;
//
//    @Column(name = "ten")
//    private String ten;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "matKhau")
//    private String matKhau;
//
//    @Column(name = "canCuoc")
//    private String canCuoc;
//
//    @Column(name = "ngaySinh")
//    private UUID ngaySinh;
//
//    @Column(name = "anhDaiDien")
//    private String anhDaiDien;
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
//    public UUID getIdVaiTro() {
//        return this.idVaiTro;
//    }
//
//    public void setIdVaiTro(UUID idVaiTro) {
//        this.idVaiTro = idVaiTro;
//    }
//
//    public String getTaiKhoan() {
//        return this.taiKhoan;
//    }
//
//    public void setTaiKhoan(String taiKhoan) {
//        this.taiKhoan = taiKhoan;
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
//    public String getEmail() {
//        return this.email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getMatKhau() {
//        return this.matKhau;
//    }
//
//    public void setMatKhau(String matKhau) {
//        this.matKhau = matKhau;
//    }
//
//    public String getCanCuoc() {
//        return this.canCuoc;
//    }
//
//    public void setCanCuoc(String canCuoc) {
//        this.canCuoc = canCuoc;
//    }
//
//    public UUID getNgaySinh() {
//        return this.ngaySinh;
//    }
//
//    public void setNgaySinh(UUID ngaySinh) {
//        this.ngaySinh = ngaySinh;
//    }
//
//    public String getAnhDaiDien() {
//        return this.anhDaiDien;
//    }
//
//    public void setAnhDaiDien(String anhDaiDien) {
//        this.anhDaiDien = anhDaiDien;
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
