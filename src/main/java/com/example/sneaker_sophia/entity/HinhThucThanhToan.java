package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HinhThucThanhToan")
public class HinhThucThanhToan {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdHoaDon")
    private Long idHoaDon;

    @Column(name = "MaHinhThuc")
    private Long maHinhThuc;

    @Column(name = "TenHinhThuc")
    private String tenHinhThuc;

    @Column(name = "SoTien")
    private Long soTien;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(Long idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public Long getMaHinhThuc() {
        return this.maHinhThuc;
    }

    public void setMaHinhThuc(Long maHinhThuc) {
        this.maHinhThuc = maHinhThuc;
    }

    public String getTenHinhThuc() {
        return this.tenHinhThuc;
    }

    public void setTenHinhThuc(String tenHinhThuc) {
        this.tenHinhThuc = tenHinhThuc;
    }

    public Long getSoTien() {
        return this.soTien;
    }

    public void setSoTien(Long soTien) {
        this.soTien = soTien;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
