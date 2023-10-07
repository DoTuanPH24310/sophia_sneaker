package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TaiKhoan_Voucher")
public class TaiKhoanVoucher {
    @Id
    @Column(name = "IdVoucher")
    private UUID idVoucher;

    @Id
    @Column(name = "IdTaiKhoan")
    private UUID idTaiKhoan;

    @Column(name = "trangThai")
    private Integer trangThai;

    public UUID getIdVoucher() {
        return this.idVoucher;
    }

    public void setIdVoucher(UUID idVoucher) {
        this.idVoucher = idVoucher;
    }

    public UUID getIdTaiKhoan() {
        return this.idTaiKhoan;
    }

    public void setIdTaiKhoan(UUID idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
