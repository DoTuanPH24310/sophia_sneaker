package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.GiayRequest;
import com.example.sneaker_sophia.dto.GiayResponse;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.GiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Service
public class GiayService {
    @Autowired
    private GiayRepository giayRepository;

//    public Page<GiayResponse> getAll(Pageable pageable) {
//        return this.giayRepository.getAll(pageable);
//    }

    public Giay add(GiayRequest giayRequest) {
        Giay giay = giayRequest.loadForm(new Giay());
        return this.giayRepository.save(giay);
    }

    public Optional<Giay> findOne(UUID id) {
        return this.giayRepository.findById(id);
    }

    public Page<Giay> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<Giay> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai.equals("-1")) {
                return giayRepository.getAll(pageable);
            }

            Giay giay = Giay.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();

            page = giayRepository.findAll(Example.of(giay), pageable);
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = giayRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = giayRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public Giay update(UUID id, GiayRequest giayRequest) {
        Optional<Giay> optional = this.giayRepository.findById(id);
        return optional.map(o -> {
            o.setId(giayRequest.getId());
            o.setMa(giayRequest.getMa());
            o.setTen(giayRequest.getTen());
            o.setTrangThai(giayRequest.getTrangThai());
            return this.giayRepository.save(o);
        }).orElse(null);
    }

    public Giay delete(UUID id){
        Optional<Giay> optional = this.giayRepository.findById(id);
        return optional.map(o ->{
            this.giayRepository.delete(o);
            return o;
        }).orElse(null);
    }


}
