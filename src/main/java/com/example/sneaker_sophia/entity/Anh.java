package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Anh")
public class Anh {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ChiTietGiay", referencedColumnName = "Id")
    private ChiTietGiay chiTietGiay;

    @Column(name = "duongDan")
    private String duongDan;

    @Column(name = "anhChinh")
    private String anhChinh;

    @Column(name = "ngayTao")
    private UUID ngayTao;

    @Column(name = "ngaySua")
    private UUID ngaySua;

    @Column(name = "nguoiTao")
    private UUID nguoiTao;

    @Column(name = "nguoiSua")
    private UUID nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;

}
