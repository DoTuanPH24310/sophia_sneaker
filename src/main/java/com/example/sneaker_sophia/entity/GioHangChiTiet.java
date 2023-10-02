package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @Column(name = "IdGioHang")
    private Long idGioHang;

    @Id
    @Column(name = "IdChiTietGiay")
    private Long idChiTietGiay;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "CreateAt")
    private LocalDateTime createAt;

    @Column(name = "UpdateAt")
    private LocalDateTime updateAt;

    public Long getIdGioHang() {
        return this.idGioHang;
    }

    public void setIdGioHang(Long idGioHang) {
        this.idGioHang = idGioHang;
    }

    public Long getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(Long idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return this.updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
