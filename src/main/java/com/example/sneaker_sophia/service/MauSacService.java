package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.MauSacRequest;
import com.example.sneaker_sophia.entity.MauSac;
import com.example.sneaker_sophia.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MauSacService {
    @Autowired
    private MauSacRepository mauSacRepository;

    public MauSac add(MauSacRequest mauSacRequest) {
        MauSac mauSac = mauSacRequest.loadForm(new MauSac());
        return this.mauSacRepository.save(mauSac);
    }

    public Optional<MauSac> findOne(UUID id) {
        return this.mauSacRepository.findById(id);
    }

    public Page<MauSac> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<MauSac> page = null;
        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai == "" || trangThai.equals("-1")) {
                return mauSacRepository.getAll(pageable);
            }

            MauSac mauSac = MauSac.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();
            page = mauSacRepository.findAll(Example.of(mauSac), pageable);

        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = mauSacRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = mauSacRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public MauSac update(UUID id, MauSacRequest mauSacRequest) {
        Optional<MauSac> optional = this.mauSacRepository.findById(id);
        return optional.map(o -> {
            o.setId(mauSacRequest.getId());
            o.setMa(mauSacRequest.getMa());
            o.setTen(mauSacRequest.getTen());
            o.setTrangThai(mauSacRequest.getTrangThai());
            return this.mauSacRepository.save(o);
        }).orElse(null);
    }

    public MauSac delete(UUID id){
        Optional<MauSac> optional = this.mauSacRepository.findById(id);
        return optional.map(o ->{
            this.mauSacRepository.delete(o);
            return o;
        }).orElse(null);
    }
}
