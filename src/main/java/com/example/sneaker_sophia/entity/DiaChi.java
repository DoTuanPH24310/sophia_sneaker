package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DiaChi")
public class DiaChi {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdNguoiDung")
    private Long idNguoiDung;

    @Column(name = "TenDiaChi")
    private String tenDiaChi;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "DiaChiCuThe")
    private String diaChiCuThe;

    @Column(name = "PhuongXa")
    private String phuongXa;

    @Column(name = "QuanHuyen")
    private String quanHuyen;

    @Column(name = "Tinh")
    private String tinh;

    @Column(name = "DiaChiMacDinh")
    private Boolean diaChiMacDinh;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdNguoiDung() {
        return this.idNguoiDung;
    }

    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getTenDiaChi() {
        return this.tenDiaChi;
    }

    public void setTenDiaChi(String tenDiaChi) {
        this.tenDiaChi = tenDiaChi;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
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

    public Boolean getDiaChiMacDinh() {
        return this.diaChiMacDinh;
    }

    public void setDiaChiMacDinh(Boolean diaChiMacDinh) {
        this.diaChiMacDinh = diaChiMacDinh;
    }
}
