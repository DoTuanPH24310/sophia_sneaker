package com.example.sneaker_sophia.entity;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CTG_KhuyenMai")
@Entity
public class CTG_KhuyenMai {

    @EmbeddedId
    private IDVoucher id;

    @Column(name = "trangThai")
    private int trangThai;
}
