package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Giay")
public class Giay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "ten")
    private String ten;

    @OneToMany(mappedBy = "giay")
    private List<ChiTietGiay> chiTietGiayList;

    public Giay() {
    }

    public Giay(String id, String ten, List<ChiTietGiay> chiTietGiayList) {
        this.id = id;
        this.ten = ten;
        this.chiTietGiayList = chiTietGiayList;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
