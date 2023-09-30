package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "NguoiDungVoucher")
public class NguoiDungVoucher {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdVoucher")
    private Long idVoucher;

    @Column(name = "IdNguoiDung")
    private Long idNguoiDung;

    @Column(name = "TrangThai")
    private Integer trangThai;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdVoucher() {
        return this.idVoucher;
    }

    public void setIdVoucher(Long idVoucher) {
        this.idVoucher = idVoucher;
    }

    public Long getIdNguoiDung() {
        return this.idNguoiDung;
    }

    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public Integer getTrangThai() {
        return this.trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }
}
