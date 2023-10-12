package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.KichCo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class KichCoRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public KichCo loadForm(KichCo kichCo) {
        kichCo.setId(getId());
        kichCo.setMa(getMa());
        kichCo.setTen(getTen());
        kichCo.setTrangThai(getTrangThai());
        return kichCo;
    }
}
