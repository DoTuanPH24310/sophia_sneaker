package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private LocalDate ngayTao;

    @Column(name = "ngaySua")
    private LocalDate ngaySua;

    @Column(name = "nguoiTao")
    private String nguoiTao;

    @Column(name = "nguoiSua")
    private String nguoiSua;

    @Column(name = "trangThai")
    private Integer trangThai;

//    @OneToMany(mappedBy = "")
//    private List<ChiTietGiay> listCTG = new ArrayList<>();

}
