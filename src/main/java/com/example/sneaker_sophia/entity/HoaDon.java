package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "Id")
    private UUID id;

    @Column(name = "IdVoucher")
    private UUID idVoucher;

    @Column(name = "IdTaiKhoan")
    private UUID idTaiKhoan;

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
    private UUID phiShip;

    @Column(name = "tienThua")
    private UUID tienThua;

    @Column(name = "tongTien")
    private UUID tongTien;

    @Column(name = "ngayTao")
    private UUID ngayTao;

    @Column(name = "ngayChuyenKhoan")
    private UUID ngayChuyenKhoan;

    @Column(name = "ngayShip")
    private UUID ngayShip;

    @Column(name = "ngayMongMuonNhan")
    private UUID ngayMongMuonNhan;

    @Column(name = "NgayNhan")
    private UUID ngayNhan;

    @Column(name = "ngaySua")
    private UUID ngaySua;

    @Column(name = "nguoiTao")
    private UUID nguoiTao;

    @Column(name = "nguoiSua")
    private UUID nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getPhiShip() {
        return this.phiShip;
    }

    public void setPhiShip(UUID phiShip) {
        this.phiShip = phiShip;
    }

    public UUID getTienThua() {
        return this.tienThua;
    }

    public void setTienThua(UUID tienThua) {
        this.tienThua = tienThua;
    }

    public UUID getTongTien() {
        return this.tongTien;
    }

    public void setTongTien(UUID tongTien) {
        this.tongTien = tongTien;
    }

    public UUID getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(UUID ngayTao) {
        this.ngayTao = ngayTao;
    }

    public UUID getNgayChuyenKhoan() {
        return this.ngayChuyenKhoan;
    }

    public void setNgayChuyenKhoan(UUID ngayChuyenKhoan) {
        this.ngayChuyenKhoan = ngayChuyenKhoan;
    }

    public UUID getNgayShip() {
        return this.ngayShip;
    }

    public void setNgayShip(UUID ngayShip) {
        this.ngayShip = ngayShip;
    }

    public UUID getNgayMongMuonNhan() {
        return this.ngayMongMuonNhan;
    }

    public void setNgayMongMuonNhan(UUID ngayMongMuonNhan) {
        this.ngayMongMuonNhan = ngayMongMuonNhan;
    }

    public UUID getNgayNhan() {
        return this.ngayNhan;
    }

    public void setNgayNhan(UUID ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public UUID getNgaySua() {
        return this.ngaySua;
    }

    public void setNgaySua(UUID ngaySua) {
        this.ngaySua = ngaySua;
    }

    public UUID getNguoiTao() {
        return this.nguoiTao;
    }

    public void setNguoiTao(UUID nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public UUID getNguoiSua() {
        return this.nguoiSua;
    }

    public void setNguoiSua(UUID nguoiSua) {
        this.nguoiSua = nguoiSua;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
