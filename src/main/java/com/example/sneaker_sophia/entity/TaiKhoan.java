package com.example.sneaker_sophia.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "TaiKhoan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Column(name = "taiKhoan")
    private String taiKhoan;

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

//    @OneToMany(mappedBy = "taiKhoan", fetch = FetchType.LAZY)
//    private List<DiaChi> diaChiList = new ArrayList<>();
//
//
//    public TaiKhoan(NhanVienRequest nhanVienRequest) {
//        this.setTen(nhanVienRequest.getTen());
//        this.setEmail(nhanVienRequest.getEmail());
//        this.setNgaySinh(nhanVienRequest.getNgaySinh());
//        this.setGioiTinh(nhanVienRequest.getGioiTinh());
//        this.setCanCuoc(nhanVienRequest.getCanCuoc());
//        this.setSdt(nhanVienRequest.getSdt());
//        this.setTrangThai(nhanVienRequest.getTrangThai());
//        this.setAnhDaiDien(nhanVienRequest.getAnhDaiDien());
//        this.setVaiTro(VaiTro.builder().id(nhanVienRequest.getIdVaiTro()).build());
//    }
}
