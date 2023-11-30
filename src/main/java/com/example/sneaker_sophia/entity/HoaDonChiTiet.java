package com.example.sneaker_sophia.entity;

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

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "HoaDonChiTiet")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class HoaDonChiTiet {
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
    private String id;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "donGia")
    private Double donGia;

    @Column(name = "phanTramGiam")
    private Integer phanTramGiam;

    @Column(name = "soLuongGiam")
    private Integer soLuongGiam;

    @CreatedBy
    @Column(name = "nguoiTao")
    private String createdBy;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdChiTietGiay")
    private ChiTietGiay chiTietGiay;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IdHoaDon")
    private HoaDon hoaDon;
}