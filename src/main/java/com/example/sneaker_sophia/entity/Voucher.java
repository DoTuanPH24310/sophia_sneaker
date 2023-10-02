package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Voucher")
public class Voucher {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Code")
    private String code;

    @Column(name = "TenVoucher")
    private String tenVoucher;

    @Column(name = "SoLuong")
    private Integer soLuong;

    @Column(name = "PhanTramGiam")
    private Long phanTramGiam;

    @Column(name = "GiaTriHoaDonToiThieu")
    private Long giaTriHoaDonToiThieu;

    @Column(name = "NgayBatDau")
    private Long ngayBatDau;

    @Column(name = "NgayKetThuc")
    private Long ngayKetThuc;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTenVoucher() {
        return this.tenVoucher;
    }

    public void setTenVoucher(String tenVoucher) {
        this.tenVoucher = tenVoucher;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Long getPhanTramGiam() {
        return this.phanTramGiam;
    }

    public void setPhanTramGiam(Long phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public Long getGiaTriHoaDonToiThieu() {
        return this.giaTriHoaDonToiThieu;
    }

    public void setGiaTriHoaDonToiThieu(Long giaTriHoaDonToiThieu) {
        this.giaTriHoaDonToiThieu = giaTriHoaDonToiThieu;
    }

    public Long getNgayBatDau() {
        return this.ngayBatDau;
    }

    public void setNgayBatDau(Long ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Long getNgayKetThuc() {
        return this.ngayKetThuc;
    }

    public void setNgayKetThuc(Long ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }
}
