package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.KichCoRequest;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class KichCoService {
    @Autowired
    private KichCoRepository kichCoRepository;

//    public Page<KichCoResponse> getAll(Pageable pageable) {
//        return this.kichCoRepository.getAll(pageable);
//    }

    public KichCo add(KichCoRequest kichCoRequest) {
        KichCo kichCo = kichCoRequest.loadForm(new KichCo());
        return this.kichCoRepository.save(kichCo);
    }

    public Optional<KichCo> findOne(UUID id) {
        return this.kichCoRepository.findById(id);
    }

    public Page<KichCo> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<KichCo> page = null;
        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai.equals("-1")) {
                return kichCoRepository.getAll(pageable);
            }

            KichCo kichCo = KichCo.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();
            page = kichCoRepository.findAll(Example.of(kichCo), pageable);
            
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = kichCoRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = kichCoRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public KichCo update(UUID id, KichCoRequest kichCoRequest) {
        Optional<KichCo> optional = this.kichCoRepository.findById(id);
        return optional.map(o -> {
            o.setId(kichCoRequest.getId());
            o.setMa(kichCoRequest.getMa());
            o.setTen(kichCoRequest.getTen());
            o.setTrangThai(kichCoRequest.getTrangThai());
            return this.kichCoRepository.save(o);
        }).orElse(null);
    }

    public KichCo delete(UUID id){
        Optional<KichCo> optional = this.kichCoRepository.findById(id);
        return optional.map(o ->{
            this.kichCoRepository.delete(o);
            return o;
        }).orElse(null);
    }
}
