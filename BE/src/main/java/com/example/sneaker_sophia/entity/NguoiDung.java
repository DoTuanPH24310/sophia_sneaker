package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NguoiDung")
public class NguoiDung {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdVaiTro")
    private Long idVaiTro;

    @Column(name = "TenNguoiDung")
    private String tenNguoiDung;

    @Column(name = "MatKhau")
    private String matKhau;

    @Column(name = "Email")
    private String email;

    @Column(name = "AnhDaiDien")
    private String anhDaiDien;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVaiTro() {
        return this.idVaiTro;
    }

    public void setIdVaiTro(Long idVaiTro) {
        this.idVaiTro = idVaiTro;
    }

    public String getTenNguoiDung() {
        return this.tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getMatKhau() {
        return this.matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
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
