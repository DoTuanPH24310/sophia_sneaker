package com.example.sneaker_sophia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class ChiTietGiayDTO {
    private UUID id;
    private String ten;
    private Double gia;
    private String ma;

}
