package com.example.sneaker_sophia.request;

import com.example.sneaker_sophia.dto.TaiKhoanDiaChi;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NhanVienRequest {

    private  String idTaiKhoan;
    
    private String idVaiTro;

    @NotBlank(message = "Không để trống tên")
    private String ten;

    private String anhDaiDien;

    private String email;

    private String canCuoc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate ngaySinh;

    private String sdt;

    private Integer gioiTinh;

    private Integer trangThai;

    private Integer tinh;

    private Integer quanHuyen;

    private Integer phuongXa;

    private String diaChiCuThe;

    public NhanVienRequest(TaiKhoanDiaChi taiKhoanDiaChi) {
        this.setIdTaiKhoan(taiKhoanDiaChi.getTaiKhoan().getId());
        this.setTen(taiKhoanDiaChi.getTaiKhoan().getTen());
        this.setAnhDaiDien(taiKhoanDiaChi.getTaiKhoan().getAnhDaiDien());
        this.setEmail(taiKhoanDiaChi.getTaiKhoan().getEmail());
        this.setCanCuoc(taiKhoanDiaChi.getTaiKhoan().getCanCuoc());
        this.setNgaySinh(taiKhoanDiaChi.getTaiKhoan().getNgaySinh());
        this.setSdt(taiKhoanDiaChi.getTaiKhoan().getSdt());
        this.setGioiTinh(taiKhoanDiaChi.getTaiKhoan().getGioiTinh());
        this.setTrangThai(taiKhoanDiaChi.getTaiKhoan().getTrangThai());
        this.setTinh(taiKhoanDiaChi.getDiaChi().getTinh());
        this.setQuanHuyen(taiKhoanDiaChi.getDiaChi().getQuanHuyen());
        this.setPhuongXa(taiKhoanDiaChi.getDiaChi().getPhuongXa());
        this.setDiaChiCuThe(taiKhoanDiaChi.getDiaChi().getDiaChiCuThe());
    }
}
