package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Anh")
public class Anh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "IdChiTietGiay")
    private Long idChiTietGiay;

    @Column(name = "TenAnh")
    private String tenAnh;

    @Column(name = "AnhChinh")
    private Boolean anhChinh;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdChiTietGiay() {
        return this.idChiTietGiay;
    }

    public void setIdChiTietGiay(Long idChiTietGiay) {
        this.idChiTietGiay = idChiTietGiay;
    }

    public String getTenAnh() {
        return this.tenAnh;
    }

    public void setTenAnh(String tenAnh) {
        this.tenAnh = tenAnh;
    }

    public Boolean getAnhChinh() {
        return this.anhChinh;
    }

    public void setAnhChinh(Boolean anhChinh) {
        this.anhChinh = anhChinh;
    }
}
