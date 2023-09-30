package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "KichCo")
public class KichCo {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Size")
    private Integer size;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
