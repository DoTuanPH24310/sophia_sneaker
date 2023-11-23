package com.example.sneaker_sophia.dto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @DateTimeFormat(pattern = "dd/mm/yyyy HH:mm")
    private LocalDateTime ngayBatDau;

    private Integer soLuong;


    @DateTimeFormat(pattern = "dd/mm/yyyy HH:mm")
    private LocalDateTime ngayKetThuc;

//    private String moTa;
}
