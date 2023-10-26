package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.HoaDon;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Data
@Getter
@Setter
public class IdHoaDonCT implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdChiTietGiay")
    private ChiTietGiay chiTietGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdHoaDon")
    private HoaDon hoaDon;
}
