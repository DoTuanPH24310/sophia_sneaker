package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoaDonChiTiet")
public class HoaDonChiTiet {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdChiTietGiay")
    private Long idChiTietGiay;

    @Column(name = "IdHoaDon")
    private Long idHoaDon;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "DonGia")
    private Long donGia;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(Long idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public Long getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(Long idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Long getDonGia() {
        return this.donGia;
    }

    public void setDonGia(Long donGia) {
        this.donGia = donGia;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
