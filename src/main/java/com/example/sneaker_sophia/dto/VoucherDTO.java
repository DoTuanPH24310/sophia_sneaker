package com.example.sneaker_sophia.dto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VoucherDTO {

    private UUID id;



    private String ten;

    private Integer phanTramGiam;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayBatDau;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngayKetThuc;

//    private String moTa;
}
