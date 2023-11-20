package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "VNPay")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class VNPay {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    @Column(name = "tienChuyenKhoan")
    private double tienChuyenKhoan;
    @Column(name = "nganHang")
    private String nganHang;
    @Column(name = "maGiaoDich")
    private String maGiaoDich;
    @Column(name = "ngayTao")
    private LocalDate ngayTao;
    @Column(name = "ngaySua")
    private LocalDate ngaySua;
    @Column(name = "nguoiTao")
    private String nguoiTao;
    @Column(name = "nguoiSua")
    private String nguoiSua;
    @Column(name = "trangThai")
    private Integer trangThai;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdHoaDon")
    private HoaDon hoaDon;


}
