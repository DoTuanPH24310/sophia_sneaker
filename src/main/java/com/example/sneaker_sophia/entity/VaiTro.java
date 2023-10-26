package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import java.util.UUID;

@Entity
@Table(name = "VaiTro")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaiTro {
    @Id
    @GenericGenerator(name = "generator", strategy = "guid", parameters = {})
    @GeneratedValue(generator = "generator")
    @Column(name = "Id", columnDefinition = "uniqueidentifier")
//    @Column(name = "Id")
//    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "ten")
    private String ten;


    @Column(name = "trangThai")
    private Integer trangThai;

//    @CreatedBy
//    @Column(name = "nguoiTao")
//    private String createdBy;
//
//    @CreatedDate
//    @Column(name = "ngayTao")
//    private LocalDateTime createdDate;
//
//    @LastModifiedBy
//    @Column(name = "nguoiSua")
//    private String updatedBy;
//
//    @LastModifiedDate
//    @Column(name = "ngaySua")
//    private LocalDateTime updatedDate;

    @OneToMany(mappedBy = "vaiTro", fetch = FetchType.LAZY)
    private List<TaiKhoan> taiKhoans = new ArrayList<>();
}