package com.example.sneaker_sophia.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "DeGiay")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
@Builder
public class DeGiay {
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

    @JsonIgnore
    @OneToMany(mappedBy = "deGiay")
    private List<ChiTietGiay> chiTietGiayList;
}
