package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "LoaiGiay")
public class LoaiGiay {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "LoaiGiay")
    private String loaiGiay;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoaiGiay() {
        return this.loaiGiay;
    }

    public void setLoaiGiay(String loaiGiay) {
        this.loaiGiay = loaiGiay;
    }
}
