package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Anh")
public class Anh {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "ChiTietGiay")
    private String chiTietGiay;

    @Column(name = "ten")
    private String ten;

    @Column(name = "anhChinh")
    private String anhChinh;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
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
