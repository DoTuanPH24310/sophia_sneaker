package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.DeGiay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class DeGiayRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public DeGiay loadForm(DeGiay deGiay) {
        deGiay.setId(getId());
        deGiay.setMa(getMa());
        deGiay.setTen(getTen());
        deGiay.setTrangThai(getTrangThai());
        return deGiay;
    }
}
