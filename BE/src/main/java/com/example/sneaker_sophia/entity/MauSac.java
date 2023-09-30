package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "MauSac")
public class MauSac {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TenMau")
    private String tenMau;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenMau() {
        return this.tenMau;
    }

    public void setTenMau(String tenMau) {
        this.tenMau = tenMau;
    }
}
