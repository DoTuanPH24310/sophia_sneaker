package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ThongBao")
public class ThongBao {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdNguoiDung")
    private Long idNguoiDung;

    @Column(name = "Kieu")
    private String kieu;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "HanhDong")
    private Integer hanhDong;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdNguoiDung() {
        return this.idNguoiDung;
    }

    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public String getKieu() {
        return this.kieu;
    }

    public void setKieu(String kieu) {
        this.kieu = kieu;
    }

    public String getTieuDe() {
        return this.tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getNoiDung() {
        return this.noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Integer getHanhDong() {
        return this.hanhDong;
    }

    public void setHanhDong(Integer hanhDong) {
        this.hanhDong = hanhDong;
    }
}
