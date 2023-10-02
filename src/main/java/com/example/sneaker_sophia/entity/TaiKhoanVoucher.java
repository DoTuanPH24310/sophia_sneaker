package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TaiKhoan_Voucher")
public class TaiKhoanVoucher {
    @Id
    @Column(name = "IdVoucher")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String idVoucher;

    @Id
    @Column(name = "IdTaiKhoan")
    private String idTaiKhoan;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getIdVoucher() {
        return this.idVoucher;
    }

    public void setIdVoucher(String idVoucher) {
        this.idVoucher = idVoucher;
    }

    public String getIdTaiKhoan() {
        return this.idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
