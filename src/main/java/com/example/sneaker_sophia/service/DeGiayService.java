package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DeGiayRequest;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeGiayService {
    @Autowired
    private DeGiayRepository deGiayRepository;

//    public Page<DeGiayResponse> getAll(Pageable pageable) {
//        return this.deGiayRepository.getAll(pageable);
//    }

    public DeGiay add(DeGiayRequest deGiayRequest) {
        DeGiay deGiay = deGiayRequest.loadForm(new DeGiay());
        return this.deGiayRepository.save(deGiay);
    }

    public Optional<DeGiay> findOne(UUID id) {
        return this.deGiayRepository.findById(id);
    }

    public Page<DeGiay> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<DeGiay> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai.equals("-1")) {
                return deGiayRepository.getAll(pageable);
            }

            DeGiay deGiay = DeGiay.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();

            page = deGiayRepository.findAll(Example.of(deGiay), pageable);
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = deGiayRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = deGiayRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public DeGiay update(UUID id, DeGiayRequest deGiayRequest) {
        Optional<DeGiay> optional = this.deGiayRepository.findById(id);
        return optional.map(o -> {
            o.setId(deGiayRequest.getId());
            o.setMa(deGiayRequest.getMa());
            o.setTen(deGiayRequest.getTen());
            o.setTrangThai(deGiayRequest.getTrangThai());
            return this.deGiayRepository.save(o);
        }).orElse(null);
    }

    public DeGiay delete(UUID id){
        Optional<DeGiay> optional = this.deGiayRepository.findById(id);
        return optional.map(o ->{
            this.deGiayRepository.delete(o);
            return o;
        }).orElse(null);
    }
}
