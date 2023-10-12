package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdVaiTro")
    private String idVaiTro;

    @Column(name = "taiKhoan")
    private String taiKhoan;

    @Column(name = "ten")
    private String ten;

    @Column(name = "email")
    private String email;

    @Column(name = "matKhau")
    private String matKhau;

    @Column(name = "canCuoc")
    private String canCuoc;

    @Column(name = "ngaySinh")
    private String ngaySinh;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdVaiTro() {
        return this.idVaiTro;
    }

    public void setIdVaiTro(String idVaiTro) {
        this.idVaiTro = idVaiTro;
    }

    public String getTaiKhoan() {
        return this.taiKhoan;
    }

    public void setTaiKhoan(String taiKhoan) {
        this.taiKhoan = taiKhoan;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return this.matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getCanCuoc() {
        return this.canCuoc;
    }

    public void setCanCuoc(String canCuoc) {
        this.canCuoc = canCuoc;
    }

    public String getNgaySinh() {
        return this.ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getAnhDaiDien() {
        return this.anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
