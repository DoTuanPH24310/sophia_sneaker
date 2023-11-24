package com.example.sneaker_sophia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

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

    @JsonIgnore
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

    @Column(name = "moTa")
    private String moTa;

    @Column(name = "ten")
    private String ten;

    @Column(name = "gia")
    private Double gia;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "trangThai")
    private Integer trangThai;

    @CreatedBy
    @Column(name = "nguoiTao")
    private String nguoiTao;

    @CreatedDate
    @Column(name = "ngayTao")
    private LocalDateTime ngayTao;

    @LastModifiedBy
    @Column(name = "nguoiSua")
    private String nguoiSua;

    @LastModifiedDate
    @Column(name = "ngaySua")
    private LocalDateTime ngaySua;

    @Column(name = "qrCode")
    private String qrCode;

    @JsonIgnore
    @OneToMany(mappedBy = "chiTietGiay")
    private List<Anh> anhs;

    @JsonIgnore
    @OneToMany(mappedBy = "chiTietGiay", fetch = FetchType.LAZY)
    private List<HoaDonChiTiet> chiTietGiayList = new ArrayList<>();


}

