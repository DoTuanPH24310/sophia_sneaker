//package com.example.sneaker_sophia.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.springframework.stereotype.Component;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Getter
//@Setter
//@Table(name = "Giay")
//@Component
//@Builder
//public class Giay {
//    @Id
//    @Column(name = "Id")
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @Column(name = "ma")
//    private String ma;
//
//    @Column(name = "ten")
//    private String ten;
//
//    @OneToMany(mappedBy = "giay", fetch = FetchType.EAGER)
//    private List<ChiTietGiay> chiTietGiayList;
//
//    @Column(name = "ngayTao")
//    private LocalDate ngayTao;
//
//    @Column(name = "ngaySua")
//    private LocalDate ngaySua;
//
//    @Column(name = "nguoiTao")
//    private UUID nguoiTao;
//
//    @Column(name = "nguoiSua")
//    private UUID nguoiSua;
//
//    @Column(name = "trangThai")
//    private Integer trangThai;
//}
