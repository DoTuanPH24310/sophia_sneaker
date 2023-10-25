package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Getter
@Table(name = "Hang")
@Builder
public class Hang {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

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