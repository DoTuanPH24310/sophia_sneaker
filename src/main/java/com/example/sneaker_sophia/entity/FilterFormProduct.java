package com.example.sneaker_sophia.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class FilterFormProduct {
        private List<Giay> giayList;
        private List<DeGiay> deGiayList;
        private List<Hang> hangList;
        private List<LoaiGiay> loaiGiayList;
        private List<MauSac> mauSacList;
        private List<KichCo> kichCoList;
//        private Double giaMin;
//        private Double giaMax;

}
