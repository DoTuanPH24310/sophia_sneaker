package com.example.sneaker_sophia.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class IdGioHangChiTiet implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdGioHang")
    private GioHang gioHang;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdChiTietGiay")
    private ChiTietGiay chiTietGiay;


}
