package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Giay")
public class Giay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ten")
    private String ten;

    @OneToMany(mappedBy = "giay", fetch = FetchType.EAGER)
    private List<ChiTietGiay> chiTietGiayList;

    public Giay() {
    }

    public Giay(UUID id, String ten, List<ChiTietGiay> chiTietGiayList) {
        this.id = id;
        this.ten = ten;
        this.chiTietGiayList = chiTietGiayList;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTen() {
        return this.ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }
}
