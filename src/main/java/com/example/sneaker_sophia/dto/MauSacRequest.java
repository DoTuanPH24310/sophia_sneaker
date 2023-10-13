package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.MauSac;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MauSacRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public MauSac loadForm(MauSac mauSac) {
        mauSac.setId(getId());
        mauSac.setMa(getMa());
        mauSac.setTen(getTen());
        mauSac.setTrangThai(getTrangThai());
        return mauSac;
    }
}
