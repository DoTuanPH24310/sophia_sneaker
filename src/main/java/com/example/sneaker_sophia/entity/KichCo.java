package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "KichCo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class KichCo {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "ngayTao")
    private Date ngayTao;

    @Column(name = "ngaySua")
    private Date ngaySua;

    @Column(name = "nguoiTao")
    private UUID nguoiTao;

    @Column(name = "nguoiSua")
    private UUID nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;

}
