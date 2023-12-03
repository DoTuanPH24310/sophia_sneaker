package com.example.sneaker_sophia.service.iplm;

import com.example.sneaker_sophia.dto.LoaiGiayRequest;
import com.example.sneaker_sophia.entity.LoaiGiay;
import com.example.sneaker_sophia.repository.LoaiGiayRepository;
import com.example.sneaker_sophia.service.LoaiGiayService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoaiGiayIplm implements LoaiGiayService2 {
    @Autowired
    LoaiGiayRepository loaiGiayRepository;

    @Override
    public List<LoaiGiay> getAll() {
        return loaiGiayRepository.findAll();
    }

    @Override
    public LoaiGiay add(LoaiGiayRequest loaiGiayRequest) {
        LoaiGiay loaiGiay = loaiGiayRequest.loadForm(new LoaiGiay());
        return this.loaiGiayRepository.save(loaiGiay);
    }

    @Override
    public Optional<LoaiGiay> findOne(UUID id) {
        return this.loaiGiayRepository.findById(id);
    }

    @Override
    public Page<LoaiGiay> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<LoaiGiay> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai == "" || trangThai.equals("-1")) {
                return loaiGiayRepository.getAll(pageable);
            }

            LoaiGiay loaiGiay = LoaiGiay.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();

            page = loaiGiayRepository.findAll(Example.of(loaiGiay), pageable);
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = loaiGiayRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = loaiGiayRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    @Override
    public LoaiGiay update(UUID id, LoaiGiayRequest loaiGiayRequest) {
        Optional<LoaiGiay> optional = this.loaiGiayRepository.findById(id);
        return optional.map(o -> {
            o.setId(loaiGiayRequest.getId());
            o.setMa(loaiGiayRequest.getMa());
            o.setTen(loaiGiayRequest.getTen());
            o.setTrangThai(loaiGiayRequest.getTrangThai());
            return this.loaiGiayRepository.save(o);
        }).orElse(null);
    }

    @Override
    public LoaiGiay delete(UUID id){
        Optional<LoaiGiay> optional = this.loaiGiayRepository.findById(id);
        return optional.map(o ->{
            o.setTrangThai(3);
            this.loaiGiayRepository.save(o);
            return o;
        }).orElse(null);
    }

    @Override
    public LoaiGiay getOne(UUID uuid) {
        return loaiGiayRepository.findById(uuid).get();
    }

    @Override
    public LoaiGiay findByTen(String ten){
        return loaiGiayRepository.findLoaiGiayByTen(ten);
    }

    @Override
    public List<LoaiGiay> findByTrangThaiEquals(Integer trangThai) {
        return loaiGiayRepository.findByTrangThaiEquals(trangThai);
    }
}

