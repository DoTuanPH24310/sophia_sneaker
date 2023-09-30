package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "HoaDon")
public class HoaDon {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdVoucher")
    private Long idVoucher;

    @Column(name = "IdNguoiDung")
    private Long idNguoiDung;

    @Column(name = "CodeHoaDon")
    private String codeHoaDon;

    @Column(name = "LoaiHoaDon")
    private Integer loaiHoaDon;

    @Column(name = "TenKhachHang")
    private String tenKhachHang;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "DiaChi")
    private String diaChi;

    @Column(name = "PhiShip")
    private Long phiShip;

    @Column(name = "TienThua")
    private Long tienThua;

    @Column(name = "TongTien")
    private Long tongTien;

    @Column(name = "NgayTao")
    private Long ngayTao;

    @Column(name = "NgayChuyenKhoan")
    private Long ngayChuyenKhoan;

    @Column(name = "NgayShip")
    private Long ngayShip;

    @Column(name = "NgayMongMuonNhan")
    private Long ngayMongMuonNhan;

    @Column(name = "NgayNhan")
    private Long ngayNhan;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVoucher() {
        return this.idVoucher;
    }

    public void setIdVoucher(Long idVoucher) {
        this.idVoucher = idVoucher;
    }

    public Long getIdNguoiDung() {
        return this.idNguoiDung;
    }

    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getCodeHoaDon() {
        return this.codeHoaDon;
    }

    public void setCodeHoaDon(String codeHoaDon) {
        this.codeHoaDon = codeHoaDon;
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

    public Long getPhiShip() {
        return this.phiShip;
    }

    public void setPhiShip(Long phiShip) {
        this.phiShip = phiShip;
    }

    public Long getTienThua() {
        return this.tienThua;
    }

    public void setTienThua(Long tienThua) {
        this.tienThua = tienThua;
    }

    public Long getTongTien() {
        return this.tongTien;
    }

    public void setTongTien(Long tongTien) {
        this.tongTien = tongTien;
    }

    public Long getNgayTao() {
        return this.ngayTao;
    }

    public void setNgayTao(Long ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Long getNgayChuyenKhoan() {
        return this.ngayChuyenKhoan;
    }

    public void setNgayChuyenKhoan(Long ngayChuyenKhoan) {
        this.ngayChuyenKhoan = ngayChuyenKhoan;
    }

    public Long getNgayShip() {
        return this.ngayShip;
    }

    public void setNgayShip(Long ngayShip) {
        this.ngayShip = ngayShip;
    }

    public Long getNgayMongMuonNhan() {
        return this.ngayMongMuonNhan;
    }

    public void setNgayMongMuonNhan(Long ngayMongMuonNhan) {
        this.ngayMongMuonNhan = ngayMongMuonNhan;
    }

    public Long getNgayNhan() {
        return this.ngayNhan;
    }

    public void setNgayNhan(Long ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
