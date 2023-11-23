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

    private String phanTramGiam;


    private Object ngayBatDau;

    private String soLuong;



    private Object ngayKetThuc;

}
