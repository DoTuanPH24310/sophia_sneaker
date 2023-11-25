package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "LoaiGiay")
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class LoaiGiay {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "ma")
    private String ma;

    @Column(name = "ten")
    private String ten;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    @Column(name = "ngaySua")
    private LocalDate ngaySua;

    @Column(name = "nguoiTao")
    private String nguoiTao;

    @Column(name = "nguoiSua")
    private String nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;

}
