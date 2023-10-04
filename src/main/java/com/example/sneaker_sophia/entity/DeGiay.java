package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "DeGiay")
public class DeGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "ten")
    private String ten;

    @OneToMany(mappedBy = "deGiay")
    private List<ChiTietGiay> chiTietGiayList;

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
