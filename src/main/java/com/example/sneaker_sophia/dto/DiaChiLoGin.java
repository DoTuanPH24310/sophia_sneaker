package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DiaChiLoGin {
    private String id;
    @NotBlank(message = "Vui lòng nhập tên người nhận")
    private String ten;
    @NotBlank(message = "Vui lòng nhập số điện thoại người nhận")
    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại không hợp lệ")
    private String sdt;
    @NotBlank(message = "Vui lòng nhập Địa chỉ cụ thể")
    private String diaChiCuThe;
    @NotNull(message = "Vui lòng chọn phường xã")
    private Integer phuongXa;
    @NotNull(message = "Vui lòng quận huyện")
    private Integer quanHuyen;
    @NotNull(message = "Vui lòng chọn tỉnh")
    private Integer tinh;

    private Integer diaChiMacDinh;

    private Integer trangThai;

    private TaiKhoan taiKhoan;

    public DiaChi loadDiaChiDTO(DiaChi diaChi) {
        diaChi.setTen(getTen());
        diaChi.setSdt(getSdt());
        diaChi.setDiaChiCuThe(getDiaChiCuThe());
        diaChi.setPhuongXa(getPhuongXa());
        diaChi.setQuanHuyen(getQuanHuyen());
        diaChi.setTinh(getTinh());
        if (getTaiKhoan() != null) {
            TaiKhoan taiKhoan = getTaiKhoan();
            diaChi.setTaiKhoan(taiKhoan);
        }

        diaChi.setDiaChiMacDinh(1);
        diaChi.setTrangThai(1);

        return diaChi;
    }
}
