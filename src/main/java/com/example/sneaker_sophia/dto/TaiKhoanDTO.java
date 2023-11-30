package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.entity.VaiTro;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaiKhoanDTO {
    private String id;

    private VaiTro vaiTro;
    @NotBlank(message = "Bạn chưa nhập tên")
    private String ten;
    @NotBlank(message = "Bạn chưa nhập email")
    private String email;

    private String matKhau;

    private String canCuoc;
    @NotBlank(message = "Bạn chưa nhập số điện thoại")
    private String sdt;
    @NotNull(message = "Bạn chưa chọn ngày sinh")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;
    @NotNull(message = "Bạn chưa chọn giới tính")
    private Integer gioiTinh;

    private String anhDaiDien;

    private Integer trangThai;

    public TaiKhoan loadFormTaiKhoan(TaiKhoan taiKhoan) {
        taiKhoan.setTen(getTen());
        taiKhoan.setEmail(getEmail());
        taiKhoan.setSdt(getSdt());
        taiKhoan.setNgaySinh(getNgaySinh());
        taiKhoan.setGioiTinh(getGioiTinh());
        return taiKhoan;
    }

}
