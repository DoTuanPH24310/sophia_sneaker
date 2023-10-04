package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ChiTietGiay")
public class ChiTietGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdGiay", referencedColumnName = "Id", insertable = false, updatable = false)
    private Giay giay;

    public ChiTietGiay() {
    }

    public Giay getGiay() {
        return giay;
    }

    public void setGiay(Giay giay) {
        this.giay = giay;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdKichCo", referencedColumnName = "Id", insertable = false, updatable = false)
    private KichCo kichCo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdDeGiay", referencedColumnName = "Id", insertable = false, updatable = false)
    private DeGiay deGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdHang", referencedColumnName = "Id", insertable = false, updatable = false)
    private Hang hang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdLoaiGiay", referencedColumnName = "Id", insertable = false, updatable = false)
    private LoaiGiay loaiGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdMauSac", referencedColumnName = "Id", insertable = false, updatable = false)
    private MauSac mauSac;

    @Column(name = "ma")
    private String ma;

    @Column(name = "gia")
    private String gia;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "trangThai")
    private Integer trangThai;

    public ChiTietGiay(String id, Giay giay, KichCo kichCo, DeGiay deGiay, Hang hang, LoaiGiay loaiGiay, MauSac mauSac, String ma, String gia, Integer soLuong, Integer trangThai) {
        this.id = id;
        this.giay = giay;
        this.kichCo = kichCo;
        this.deGiay = deGiay;
        this.hang = hang;
        this.loaiGiay = loaiGiay;
        this.mauSac = mauSac;
        this.ma = ma;
        this.gia = gia;
        this.soLuong = soLuong;
        this.trangThai = trangThai;
    }

    public KichCo getKichCo() {
        return kichCo;
    }

    public void setKichCo(KichCo kichCo) {
        this.kichCo = kichCo;
    }

    public DeGiay getDeGiay() {
        return deGiay;
    }

    public void setDeGiay(DeGiay deGiay) {
        this.deGiay = deGiay;
    }

    public Hang getHang() {
        return hang;
    }

    public void setHang(Hang hang) {
        this.hang = hang;
    }

    public LoaiGiay getLoaiGiay() {
        return loaiGiay;
    }

    public void setLoaiGiay(LoaiGiay loaiGiay) {
        this.loaiGiay = loaiGiay;
    }

    public MauSac getMauSac() {
        return mauSac;
    }

    public void setMauSac(MauSac mauSac) {
        this.mauSac = mauSac;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMa() {
        return this.ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getGia() {
        return this.gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public Integer getSoLuong() {
        return this.soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
