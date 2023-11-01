package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "Id")
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdVoucher")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdTaiKhoan")
    private TaiKhoan taiKhoan;

    @Column(name = "maHoaDOn")
    private String maHoaDOn;

    @Column(name = "loaiHoaDon")
    private Integer loaiHoaDon;

    @Column(name = "tenKhachHang")
    private String tenKhachHang;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "phiShip")
    private Double phiShip;

    @Column(name = "tienThua")
    private Double tienThua;

    @Column(name = "tongTien")
    private Double tongTien;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "ngayChuyenKhoan")
    private LocalDate ngayChuyenKhoan;

    @Column(name = "ngayShip")
    private LocalDate ngayShip;

    @Column(name = "ngayMongMuonNhan")
    private LocalDate ngayMongMuonNhan;

    @Column(name = "NgayNhan")
    private LocalDate ngayNhan;

    @Column(name = "ngaySua")
    private LocalDate ngaySua;

    @Column(name = "nguoiTao")
    private UUID nguoiTao;

    @Column(name = "nguoiSua")
    private UUID nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;


}
