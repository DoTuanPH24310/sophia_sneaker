package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "GioHangChiTiet")
public class GioHangChiTiet {
    @Id
    @Column(name = "IdGioHang")
    private UUID idGioHang;

    @Id
    @Column(name = "IdChiTietGiay")
    private UUID idChiTietGiay;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "ngaySua")
    private LocalDate ngaySua;

    @Column(name = "nguoiTao")
    private String nguoiTao;

    @Column(name = "nguoiSua")
    private String nguoiSua;

}
