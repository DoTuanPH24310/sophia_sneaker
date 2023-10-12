package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "GioHang")
public class GioHang {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdTaiKhoan")
    private String idTaiKhoan;

    @Column(name = "ngayTao")
    private String ngayTao;

    @Column(name = "ngaySua")
    private String ngaySua;

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

    public String getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgaySua() {
        return this.ngaySua;
    }

    public void setNgaySua(String ngaySua) {
        this.ngaySua = ngaySua;
    }
}
