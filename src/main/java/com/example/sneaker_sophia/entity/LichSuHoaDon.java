package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LichSuHoaDon")
public class LichSuHoaDon {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdHoaDon")
    private Long idHoaDon;

    @Column(name = "GhiChu")
    private String ghiChu;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdHoaDon() {
        return this.idHoaDon;
    }

    public void setIdHoaDon(Long idHoaDon) {
        this.idHoaDon = idHoaDon;
    }

    public String getGhiChu() {
        return this.ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
