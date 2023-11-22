package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.Hang;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class HangRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public Hang loadForm(Hang hang) {
        hang.setId(getId());
        hang.setMa(getMa());
        hang.setTen(getTen());
        hang.setTrangThai(getTrangThai());
        return hang;
    }
}
