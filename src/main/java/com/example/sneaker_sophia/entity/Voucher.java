package com.example.sneaker_sophia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@EntityListeners(AuditingEntityListener.class)
@ToString
@Table(name = "KhuyenMai")
public class Voucher {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonIgnore
    @OneToMany(mappedBy = "id.voucher")
    private List<CTG_KhuyenMai> listCTG_KM;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "soLuong")
    private Integer soLuong;

    @Column(name = "phanTramGiam")
    private Integer phanTramGiam;

    @Column(name = "soLuongGiam")
    private Integer soLuongGiam;

    @Column(name = "ngayBatDau")
    private LocalDateTime ngayBatDau;

    @Column(name = "ngayKetThuc")
    private LocalDateTime ngayKetThuc;

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

}
