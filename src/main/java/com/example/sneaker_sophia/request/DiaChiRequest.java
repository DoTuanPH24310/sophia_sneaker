package com.example.sneaker_sophia.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiRequest {
    private Integer tinh;

    private Integer quanHuyen;

    private Integer phuongXa;

    private String diaChiCuThe;
}
