package com.example.sneaker_sophia.entity;

import com.example.sneaker_sophia.request.NhanVienRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "DiaChi")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiaChi {
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "ten")
    private String ten;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "diaChiCuThe")
    private String diaChiCuThe;

    @Column(name = "phuongXa")
    private Integer phuongXa;

    @Column(name = "quanHuyen")
    private Integer quanHuyen;

    @Column(name = "tinh")
    private Integer tinh;

    @Column(name = "diaChiMacDinh")
    private Integer diaChiMacDinh;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdTaiKhoan")
    private TaiKhoan taiKhoan;

    public DiaChi(NhanVienRequest nhanVienRequest) {
        this.setDiaChiCuThe(nhanVienRequest.getDiaChiCuThe());
        this.setTinh(nhanVienRequest.getTinh());
        this.setQuanHuyen(nhanVienRequest.getQuanHuyen());
        this.setPhuongXa(nhanVienRequest.getPhuongXa());
        this.setTaiKhoan(TaiKhoan.builder().id(nhanVienRequest.getIdTaiKhoan()).build());
        this.setDiaChiMacDinh(1);
        this.setTrangThai(1);
    }
}
