package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Range;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ChiTietGiayDTO {

    private UUID id;

    private Giay giay;
    private KichCo kichCo;

    private DeGiay deGiay;

    private Hang hang;

    private LoaiGiay loaiGiay;

    private MauSac mauSac;

    @NotBlank(message = "Không được để trống mã")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Mã không có kí tự đặc biệt.")
    @Size(max = 20, message = "Mã không được vượt quá 20 kí tự.")
    private String ma;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 kí tự.")
    private String moTa;

    @NotBlank(message = "Không được để trống tên")
    @Size(max = 50, message = "Tên không được vượt quá 50 kí tự.")
    private String ten;

    @Digits(integer = Integer.MAX_VALUE, fraction = 2, message = "Giá phải là số và tối đa 2 chữ số sau dấu phẩy.")
    @NotNull(message = "Giá không được để trống.")
    @Min(value = 0, message = "Giá phải là một số không âm.")
    @Max(value = 1000000000, message = "Số lượng phải bé hơn 1.000.000.000")
    private Double gia;

    @NotNull(message = "Số lượng không được để trống.")
    @Digits(integer = Integer.MAX_VALUE, fraction = 0, message = "Giá phải là số.")
    @Min(value = 0, message = "Số lượng phải là một số không âm.")
    @Max(value = 10000, message = "Số lượng phải bé hơn 10000.")
    private Integer soLuong;

    private Integer trangThai;

    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "QR Code không có kí tự đặc biệt.")
    @Size(max = 16, message = "QR không được vượt quá 16 kí tự.")
    private String qrCode;

    @Size(min = 2, max = 3, message = "Danh sách hình ảnh phải chứa ít nhất 2 và ít hơn 3 hình ảnh.")
    @Valid
    private List<Anh> anhs;

    public ChiTietGiay loadChiTietGiayDTO(ChiTietGiayDTO chiTietGiayDto) {
        ChiTietGiay chiTietGiay = new ChiTietGiay();
        chiTietGiay.setId(chiTietGiayDto.getId());
        chiTietGiay.setMa(chiTietGiayDto.getMa());
        chiTietGiay.setTen(chiTietGiayDto.getTen());
        chiTietGiay.setGiay(chiTietGiayDto.getGiay());
        chiTietGiay.setDeGiay(chiTietGiayDto.getDeGiay());
        chiTietGiay.setLoaiGiay(chiTietGiayDto.getLoaiGiay());
        chiTietGiay.setMauSac(chiTietGiayDto.getMauSac());
        chiTietGiay.setHang(chiTietGiayDto.getHang());
        chiTietGiay.setKichCo(chiTietGiayDto.getKichCo());
        chiTietGiay.setSoLuong(chiTietGiayDto.getSoLuong());
        chiTietGiay.setGia(chiTietGiayDto.getGia());
        chiTietGiay.setQrCode(chiTietGiayDto.getQrCode());
        chiTietGiay.setTrangThai(chiTietGiayDto.getTrangThai());
        chiTietGiay.setMoTa(chiTietGiayDto.getMoTa());
        chiTietGiay.setAnhs(chiTietGiayDto.getAnhs());

        return chiTietGiay;
    }

}
