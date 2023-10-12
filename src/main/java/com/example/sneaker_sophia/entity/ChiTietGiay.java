package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @Column(name = "ma")
    private String ma;

    @Column(name = "gia")
    private Double gia;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "trangThai")
    private Integer trangThai;


}

