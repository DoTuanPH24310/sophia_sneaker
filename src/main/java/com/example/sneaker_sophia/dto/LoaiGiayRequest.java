package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.LoaiGiay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LoaiGiayRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public LoaiGiay loadForm(LoaiGiay loaiGiay) {
        loaiGiay.setId(getId());
        loaiGiay.setMa(getMa());
        loaiGiay.setTen(getTen());
        loaiGiay.setTrangThai(getTrangThai());
        return loaiGiay;
    }
}
