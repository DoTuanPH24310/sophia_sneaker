package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DiaChi")
public class DiaChi {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdTaiKhoan")
    private String idTaiKhoan;

    @Column(name = "ten")
    private String ten;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "diaChiCuThe")
    private String diaChiCuThe;

    @Column(name = "phuongXa")
    private String phuongXa;

    @Column(name = "quanHuyen")
    private String quanHuyen;

    @Column(name = "tinh")
    private String tinh;

    @Column(name = "diaChiMacDinh")
    private String diaChiMacDinh;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTaiKhoan() {
        return this.idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSdt() {
        return this.sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChiCuThe() {
        return this.diaChiCuThe;
    }

    public void setDiaChiCuThe(String diaChiCuThe) {
        this.diaChiCuThe = diaChiCuThe;
    }

    public String getPhuongXa() {
        return this.phuongXa;
    }

    public void setPhuongXa(String phuongXa) {
        this.phuongXa = phuongXa;
    }

    public String getQuanHuyen() {
        return this.quanHuyen;
    }

    public void setQuanHuyen(String quanHuyen) {
        this.quanHuyen = quanHuyen;
    }

    public String getTinh() {
        return this.tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public String getDiaChiMacDinh() {
        return this.diaChiMacDinh;
    }

    public void setDiaChiMacDinh(String diaChiMacDinh) {
        this.diaChiMacDinh = diaChiMacDinh;
    }
}
