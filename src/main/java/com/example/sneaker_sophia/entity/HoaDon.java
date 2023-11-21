package com.example.sneaker_sophia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "HoaDon")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon {
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdTaiKhoan")
    private TaiKhoan taiKhoan;

    @Column(name = "maHoaDOn")
    private String maHoaDOn;

    @Column(name = "loaiHoaDon")
    private Integer loaiHoaDon;

    @Column(name = "tenKhachHang")
    private String tenKhachHang;

    @Column(name = "soDienThoai")
    private String soDienThoai;

    @Column(name = "diaChi")
    private String diaChi;

    @Column(name = "phiShip")
    private Double phiShip;

    @Column(name = "tienThua")
    private Double tienThua;

    @Column(name = "tongTien")
    private Double tongTien;



    @Column(name = "ngayShip")
    private LocalDate ngayShip;

    @Column(name = "ngayMongMuonNhan")
    private LocalDate ngayMongMuonNhan;

    @Column(name = "NgayNhan")
    private LocalDate ngayNhan;

    @Column(name = "khuyenMai")
    private Double khuyenMai;

    @Column(name = "ghiChu")
    private String ghiChu;

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

    @Column(name = "trangThai")
    private Integer trangThai;

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.LAZY)
    private List<HoaDonChiTiet> listHoaDonChiTiet = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.LAZY)
    private List<HinhThucThanhToan> hinhThucThanhToanList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "hoaDon", fetch = FetchType.LAZY)
    private List<LichSuHoaDon> lichSuHoaDons = new ArrayList<>();
}