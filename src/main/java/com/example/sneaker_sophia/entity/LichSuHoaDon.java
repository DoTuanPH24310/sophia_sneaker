package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LichSuHoaDon")
public class LichSuHoaDon {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdHoaDon")
    private String idHoaDon;

    @Column(name = "thoiGian")
    private String thoiGian;

    @Column(name = "ghiChu")
    private String ghiChu;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(String idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public String getThoiGian() {
        return this.thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getGhiChu() {
        return this.ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
