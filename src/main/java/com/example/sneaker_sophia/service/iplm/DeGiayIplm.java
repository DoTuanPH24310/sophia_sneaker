package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.dto.DeGiayRequest;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.DeGiayRepository;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.DeGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeGiayIplm implements DeGiayService {
    @Autowired
    DeGiayRepository deGiayRepository;
    @Override
    public List<DeGiay> getAll() {
        return deGiayRepository.findAll();
    }

    @Override
    public DeGiay add(DeGiayRequest deGiayRequest) {
        DeGiay deGiay = deGiayRequest.loadForm(new DeGiay());
        return this.deGiayRepository.save(deGiay);
    }

    @Override
    public Optional<DeGiay> findOne(UUID id) {
        return this.deGiayRepository.findById(id);
    }

    @Override
    public Page<DeGiay> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<DeGiay> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai == "" || trangThai.equals("-1")) {
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

    @Override
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

    @Override
    public DeGiay delete(UUID id){
        Optional<DeGiay> optional = this.deGiayRepository.findById(id);
        return optional.map(o ->{
            this.deGiayRepository.delete(o);
            return o;
        }).orElse(null);
    }
}
