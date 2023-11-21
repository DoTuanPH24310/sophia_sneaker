package com.example.sneaker_sophia.dto;


import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.request.NhanVienRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DiaChiDTO {
    private String id;
    @NotBlank(message = "Vui lòng nhập tên người nhận")
    private String ten;
    @NotBlank(message = "Vui lòng nhập số điện thoại người nhận")

    private String sdt;
    @NotBlank(message = "Vui lòng nhập Địa chỉ cụ thể")
    private String diaChiCuThe;
    @NotNull(message = "Vui lòng chọn phường xã")
    private Integer phuongXa;
    @NotNull(message = "Vui lòng quận huyện")
    private Integer quanHuyen;
    @NotNull(message = "Vui lòng chọn tỉnh")
    private Integer tinh;
    @NotBlank(message = "Vui lòng nhập email")
    @Email(message = "Email không đúng định dạng")
    private String email;

    private Integer diaChiMacDinh;

    private Integer trangThai;

    private TaiKhoan taiKhoan;

    public DiaChi loadDiaChiDTO(DiaChi diaChi) {
        diaChi.setId(getId());
        diaChi.setTen(getTen());
        diaChi.setSdt(getSdt());
        diaChi.setDiaChiCuThe(getDiaChiCuThe());
        diaChi.setPhuongXa(getPhuongXa());
        diaChi.setQuanHuyen(getQuanHuyen());
        diaChi.setTinh(getTinh());
        diaChi.getTaiKhoan().setEmail(getEmail());
        if (getTaiKhoan() != null) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setId(getTaiKhoan().getId());
            diaChi.setTaiKhoan(taiKhoan);
        }

        diaChi.setDiaChiMacDinh(1);
        diaChi.setTrangThai(1);

        return diaChi;
    }

}
