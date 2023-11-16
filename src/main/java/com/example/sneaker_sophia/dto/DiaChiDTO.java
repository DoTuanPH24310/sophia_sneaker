package com.example.sneaker_sophia.dto;


import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.request.NhanVienRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiaChiDTO {
    private String id;

    private String ten;

    private String sdt;

    private String diaChiCuThe;

    private Integer phuongXa;

    private Integer quanHuyen;

    private Integer tinh;

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
