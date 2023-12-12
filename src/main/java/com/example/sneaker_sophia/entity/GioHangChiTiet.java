    package com.example.sneaker_sophia.entity;

    import jakarta.persistence.*;
    import lombok.*;

    import java.time.LocalDate;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @Entity
    @Table(name = "GioHangChiTiet")
    public class GioHangChiTiet {
        @EmbeddedId
        private IdGioHangChiTiet id;

        @Column(name = "soLuong")
        private Integer soLuong;

        @Column(name = "ngayTao")
        private LocalDate ngayTao;

        @Column(name = "ngaySua")
        private LocalDate ngaySua;

        @Column(name = "nguoiTao")
        private String nguoiTao;

        @Column(name = "nguoiSua")
        private String nguoiSua;

        public GioHangChiTiet(IdGioHangChiTiet id, Integer soLuong) {
            this.id = id;
            this.soLuong = soLuong;
        }

    }
