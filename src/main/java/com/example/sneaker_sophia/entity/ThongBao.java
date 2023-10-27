package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ThongBao")
public class ThongBao {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdTaiKhoan")
    private String idTaiKhoan;

    @Column(name = "tieuDe")
    private String tieuDe;

    @Column(name = "kieu")
    private String kieu;

    @Column(name = "noiDung")
    private String noiDung;

    @Column(name = "hanhDong")
    private String hanhDong;

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

    public String getTieuDe() {
        return this.tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getKieu() {
        return this.kieu;
    }

    public void setKieu(String kieu) {
        this.kieu = kieu;
    }

    public String getNoiDung() {
        return this.noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getHanhDong() {
        return this.hanhDong;
    }

    public void setHanhDong(String hanhDong) {
        this.hanhDong = hanhDong;
    }
}
