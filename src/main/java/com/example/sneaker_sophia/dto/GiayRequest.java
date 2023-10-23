package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.Giay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Getter
@Setter
public class GiayRequest {
    private UUID id;
    @NotBlank(message = "Vui long nhap ma")
    private String ma;
    @NotBlank(message = "Vui long nhap ten")
    private String ten;
    @NotNull(message = "Vui long chọn trạng thái")
    private Integer trangThai;

    public Giay loadForm(Giay giay) {
        giay.setId(getId());
        giay.setMa(getMa());
        giay.setTen(getTen());
        giay.setTrangThai(getTrangThai());
        return giay;
    }
}