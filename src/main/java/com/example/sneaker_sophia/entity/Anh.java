package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "Anh")
public class Anh {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ChiTietGiay")
    private String chiTietGiay;

    @Column(name = "ten")
    private String ten;

    @Column(name = "anhChinh")
    private String anhChinh;

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getChiTietGiay() {
        return this.chiTietGiay;
    }

    public void setChiTietGiay(String chiTietGiay) {
        this.chiTietGiay = chiTietGiay;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getAnhChinh() {
        return this.anhChinh;
    }

    public void setAnhChinh(String anhChinh) {
        this.anhChinh = anhChinh;
    }
}
