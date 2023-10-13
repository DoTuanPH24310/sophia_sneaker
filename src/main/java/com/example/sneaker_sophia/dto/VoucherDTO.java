package com.example.sneaker_sophia.dto;


import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VoucherDTO {

    @NotBlank(message = "Vui lòng nhập tên khuyến mại")
    private String ten;

    @NotNull(message = "Vui lòng nhập giá trị(%) ")
    private Integer giaTri;

    @DateTimeFormat(pattern = "dd-MM-yyy:hh:mm")
    @Temporal(TemporalType.DATE)
    @NotNull(message = "Vui lòng chọn ngày bắt đầu...")
    private LocalDate ngayBatDau;


    @NotNull(message = "Vui lòng chọn ngày kết thúc")
    private LocalDate ngayKetThuc;

    private String moTa;
}
