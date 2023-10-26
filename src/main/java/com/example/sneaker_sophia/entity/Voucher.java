//package com.example.sneaker_sophia.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.UUID;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Getter
//@Setter
//@Builder
//@Table(name = "KhuyenMai")
//public class Voucher {
//    @Id
//    @Column(name = "Id")
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @OneToMany(mappedBy = "id.voucher")
//    private List<CTG_KhuyenMai> listCTG_KM;
//
//    @Column(name = "ma")
//    private String ma;
//
//    @Column(name = "ten")
//    private String ten;
//
//    @Column(name = "soLuong")
//    private Integer soLuong;
//
//    @Column(name = "phanTramGiam")
//    private Integer phanTramGiam;
//
//    @Column(name = "ngayBatDau")
//    private LocalDate ngayBatDau;
//
//    @Column(name = "ngayKetThuc")
//    private LocalDate ngayKetThuc;
//
//    @Column(name = "ngayTao")
//    private LocalDate ngayTao;
//
//    @Column(name = "ngaySua")
//    private LocalDate ngaySua;
//
////    @Column(name = "nguoiTao")
////    private UUID nguoiTao;
////
////    @Column(name = "nguoiSua")
////    private UUID nguoiSua;
//
//    @Column(name = "trangThai")
//    private Integer trangThai;
//
//
//}
