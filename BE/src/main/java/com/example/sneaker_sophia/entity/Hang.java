package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Hang")
public class Hang {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TenHang")
    private String tenHang;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenHang() {
        return this.tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }
}
