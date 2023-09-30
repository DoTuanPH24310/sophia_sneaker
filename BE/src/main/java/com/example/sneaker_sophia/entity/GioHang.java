package com.example.sneaker_sophia.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "GioHang")
public class GioHang {
    @Id
    @Column(name = "Id")@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "IdNguoiDung")
    private Long idNguoiDung;

    @Column(name = "CreateAt")
    private LocalDateTime createAt;

    @Column(name = "UpdateAt")
    private LocalDateTime updateAt;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdNguoiDung() {
        return this.idNguoiDung;
    }

    public void setIdNguoiDung(Long idNguoiDung) {
        this.idNguoiDung = idNguoiDung;
    }

    public LocalDateTime getCreateAt() {
        return this.createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return this.updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
