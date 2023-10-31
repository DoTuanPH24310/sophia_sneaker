package com.example.sneaker_sophia.dto;

import com.example.sneaker_sophia.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FillterDTO {

    private UUID id;

    private String giay;

    private String deGiay;

    private double giaMax;

    private double giaMin;

    private String hang;

    private String loaiGiay;

    private String mauSac;

    private String kichCo;

    private boolean isFilterApplied = false;

    public FillterDTO() {
    }

    public FillterDTO(UUID id, String giay, String deGiay, double giaMax, double giaMin, String hang, String loaiGiay, String mauSac, String kichCo) {
        this.id = id;
        this.giay = giay;
        this.deGiay = deGiay;
        this.giaMax = giaMax;
        this.giaMin = giaMin;
        this.hang = hang;
        this.loaiGiay = loaiGiay;
        this.mauSac = mauSac;
        this.kichCo = kichCo;

        if (giay != null && !giay.isEmpty()) {
            this.isFilterApplied = true;
        }

        if (deGiay != null && !deGiay.isEmpty()) {
            this.isFilterApplied = true;
        }

        if (loaiGiay != null && !loaiGiay.isEmpty()) {
            this.isFilterApplied = true;
        }

        if (mauSac != null && !mauSac.isEmpty()) {
            this.isFilterApplied = true;
        }

        if (kichCo != null && !kichCo.isEmpty()) {
            this.isFilterApplied = true;
        }

        if (hang != null && !hang.isEmpty()){
            this.isFilterApplied = true;
        }

        if (giaMax != 0.0 || giaMin != 0.0) {
            this.isFilterApplied = true;
        }
    }

    public void setGiay(String giay) {
        this.giay = giay;

        if (giay != null && !giay.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }

    public void setDeGiay(String deGiay) {
        this.deGiay = deGiay;

        if (deGiay != null && !deGiay.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }

    public void setHang(String hang) {
        this.hang = hang;

        if (hang != null && !hang.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }

    public void setLoaiGiay(String loaiGiay) {
        this.loaiGiay = loaiGiay;

        if (loaiGiay != null && !loaiGiay.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }

    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;

        if (mauSac != null && !mauSac.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }

    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;

        if (kichCo != null && !kichCo.isEmpty()) {
            this.isFilterApplied = true;
        } else {
            this.isFilterApplied = false;
        }
    }



}

