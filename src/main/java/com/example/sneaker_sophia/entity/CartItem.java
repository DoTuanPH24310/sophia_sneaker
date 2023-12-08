package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@ToString
public class CartItem {
    private UUID id;

    private String anh;

    private String giay;

    private String ten;

    private String hang;

    private String loaiGiay;

    private String mauSac;
    private String kichCo;


    private Integer soLuong;

    private Double gia;


}
