package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DeGiay")
public class DeGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TenDeGiay")
    private String tenDeGiay;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDeGiay() {
        return this.tenDeGiay;
    }

    public void setTenDeGiay(String tenDeGiay) {
        this.tenDeGiay = tenDeGiay;
    }
}
