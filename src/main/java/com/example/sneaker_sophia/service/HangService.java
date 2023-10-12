package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.HangRequest;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class HangService {
    @Autowired
    private HangRepository hangRepository;

//    public Page<HangResponse> getAll(Pageable pageable) {
//        return this.hangRepository.getAll(pageable);
//    }

    public Hang add(HangRequest hangRequest) {
        Hang hang = hangRequest.loadForm(new Hang());
        return this.hangRepository.save(hang);
    }

    public Optional<Hang> findOne(UUID id) {
        return this.hangRepository.findById(id);
    }

    public Page<Hang> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<Hang> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai.equals("-1")) {
                return hangRepository.getAll(pageable);
            }

            Hang hang = Hang.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();

            page = hangRepository.findAll(Example.of(hang), pageable);
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = hangRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = hangRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public Hang update(UUID id, HangRequest hangRequest) {
        Optional<Hang> optional = this.hangRepository.findById(id);
        return optional.map(o -> {
            o.setId(hangRequest.getId());
            o.setMa(hangRequest.getMa());
            o.setTen(hangRequest.getTen());
            o.setTrangThai(hangRequest.getTrangThai());
            return this.hangRepository.save(o);
        }).orElse(null);
    }

    public Hang delete(UUID id){
        Optional<Hang> optional = this.hangRepository.findById(id);
        return optional.map(o ->{
            this.hangRepository.delete(o);
            return o;
        }).orElse(null);
    }
}
