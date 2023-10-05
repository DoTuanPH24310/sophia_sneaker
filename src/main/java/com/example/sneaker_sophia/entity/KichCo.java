package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "KichCo")
public class KichCo {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ten")
    private String ten;

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
