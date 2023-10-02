package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "IdVoucher")
    private String idVoucher;

    @Column(name = "IdTaiKhoan")
    private String idTaiKhoan;

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
    private String phiShip;

    @Column(name = "tienThua")
    private String tienThua;

    @Column(name = "tongTien")
    private String tongTien;

    @Column(name = "ngayTao")
    private String ngayTao;

    @Column(name = "ngayChuyenKhoan")
    private String ngayChuyenKhoan;

    @Column(name = "ngayShip")
    private String ngayShip;

    @Column(name = "ngayMongMuonNhan")
    private String ngayMongMuonNhan;

    @Column(name = "NgayNhan")
    private String ngayNhan;

    @Column(name = "trangThai")
    private Integer trangThai;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getMaHoaDOn() {
        return this.maHoaDOn;
    }

    public void setMaHoaDOn(String maHoaDOn) {
        this.maHoaDOn = maHoaDOn;
    }

    public Integer getLoaiHoaDon() {
        return this.loaiHoaDon;
    }

    public void setLoaiHoaDon(Integer loaiHoaDon) {
        this.loaiHoaDon = loaiHoaDon;
    }

    public String getTenKhachHang() {
        return this.tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return this.soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return this.diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getPhiShip() {
        return this.phiShip;
    }

    public void setPhiShip(String phiShip) {
        this.phiShip = phiShip;
    }

    public String getTienThua() {
        return this.tienThua;
    }

    public void setTienThua(String tienThua) {
        this.tienThua = tienThua;
    }

    public String getTongTien() {
        return this.tongTien;
    }

    public void setTongTien(String tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(String ngayTao) {
        this.ngayTao = ngayTao;
    }

    public String getNgayChuyenKhoan() {
        return this.ngayChuyenKhoan;
    }

    public void setNgayChuyenKhoan(String ngayChuyenKhoan) {
        this.ngayChuyenKhoan = ngayChuyenKhoan;
    }

    public String getNgayShip() {
        return this.ngayShip;
    }

    public void setNgayShip(String ngayShip) {
        this.ngayShip = ngayShip;
    }

    public String getNgayMongMuonNhan() {
        return this.ngayMongMuonNhan;
    }

    public void setNgayMongMuonNhan(String ngayMongMuonNhan) {
        this.ngayMongMuonNhan = ngayMongMuonNhan;
    }

    public String getNgayNhan() {
        return this.ngayNhan;
    }

    public void setNgayNhan(String ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
