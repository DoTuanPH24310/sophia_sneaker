package com.example.sneaker_sophia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "ChiTietGiay")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChiTietGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdGiay", referencedColumnName = "Id")
    private Giay giay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdKichCo")
    private KichCo kichCo;

    @OneToMany(mappedBy = "id.chiTietGiay")
    private List<CTG_KhuyenMai> listCTG_KM;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdDeGiay", referencedColumnName = "Id")
    private DeGiay deGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdHang", referencedColumnName = "Id")
    private Hang hang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdLoaiGiay", referencedColumnName = "Id")
    private LoaiGiay loaiGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdMauSac", referencedColumnName = "Id")
    private MauSac mauSac;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "gia")
    private Double gia;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "trangThai")
    private Integer trangThai;

    @Column(name = "qrCode")
    private String qrCode;

    @JsonIgnore
    @OneToMany(mappedBy = "chiTietGiay")
    private List<Anh> anhs;

    @JsonIgnore
    @OneToMany(mappedBy = "chiTietGiay", fetch = FetchType.LAZY)
    private List<HoaDonChiTiet> chiTietGiayList = new ArrayList<>();

}

