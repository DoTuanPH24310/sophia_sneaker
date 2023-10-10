package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Table(name = "Voucher")
public class Voucher {
    @Id
    @Column(name = "Id")
//    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "phanTramGiam")
    private Double phanTramGiam;

    @Column(name = "soTienGiam")
    private Double soTienGiam;

    @Column(name = "giaTriToiThieu")
    private Double giaTriToiThieu;

    @Column(name = "ngayBatDau")
    private LocalDate ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDate ngayKetThuc;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "ngaySua")
    private LocalDate ngaySua;

//    @Column(name = "nguoiTao")
//    private UUID nguoiTao;
//
//    @Column(name = "nguoiSua")
//    private UUID nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;


}
