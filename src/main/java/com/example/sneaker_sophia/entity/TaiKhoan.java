package com.example.sneaker_sophia.entity;

import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TaiKhoan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class TaiKhoan {
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
//    @Column(name = "Id")
//    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @JoinColumn(name = "IdVaiTro")
    @ManyToOne(fetch = FetchType.EAGER)
    private VaiTro vaiTro;

    @JsonIgnore
    @OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
    List<HoaDon> hoaDons = new ArrayList<>();

    @Column(name = "ten")
    private String ten;

    @Column(name = "email")
    private String email;

    @Column(name = "matKhau")
    private String matKhau;

    @Column(name = "canCuoc")
    private String canCuoc;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "ngaySinh")
    private LocalDate ngaySinh;

    @Column(name = "gioitinh")
    private Integer gioiTinh;

    @Column(name = "anhDaiDien")
    private String anhDaiDien;

    @Column(name = "trangThai")
    private Integer trangThai;

    @CreatedBy
    @Column(name = "nguoiTao")
    private String createdBy;

    @CreatedDate
    @Column(name = "ngayTao")
    private LocalDateTime createdDate;

    @LastModifiedBy
    @Column(name = "nguoiSua")
    private String updatedBy;

    @LastModifiedDate
    @Column(name = "ngaySua")
    private LocalDateTime updatedDate;

    @JsonIgnore
    @OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
    private List<DiaChi> diaChiList = new ArrayList<>();

    public TaiKhoan(TaiKhoanRequest nhanVienRequest) {
        this.setTen(nhanVienRequest.getTen());
        this.setEmail(nhanVienRequest.getEmail());
        if (!nhanVienRequest.getNgaySinh().equals("")){
            this.setNgaySinh(LocalDate.parse(nhanVienRequest.getNgaySinh()));
        }
        this.setGioiTinh(Integer.parseInt(nhanVienRequest.getGioiTinh()));
        this.setCanCuoc(nhanVienRequest.getCanCuoc());
        this.setSdt(nhanVienRequest.getSdt());
        this.setTrangThai(nhanVienRequest.getTrangThai());
        this.setAnhDaiDien(nhanVienRequest.getAnhDaiDien());
        this.setVaiTro(VaiTro.builder().id(nhanVienRequest.getIdVaiTro()).build());
    }

    public void getTaiKhoanKH(TaiKhoanRequest taiKhoanRequest){
        this.setTen(taiKhoanRequest.getTen());
        this.setEmail(taiKhoanRequest.getEmail());
        this.setSdt(taiKhoanRequest.getSdt());
        this.setMatKhau(taiKhoanRequest.getMatKhau());
        this.setVaiTro(VaiTro.builder().id(taiKhoanRequest.getIdVaiTro()).build());
    }

}