package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.KichCoRequest;
import com.example.sneaker_sophia.entity.KichCo;
import com.example.sneaker_sophia.repository.KichCoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface KichCoService {

//    public Page<KichCoResponse> getAll(Pageable pageable) {
//        return this.kichCoRepository.getAll(pageable);
//    }

    List<KichCo> getAll();

    KichCo add(KichCoRequest kichCoRequest);

    Optional<KichCo> findOne(UUID id);

    Page<KichCo> fillter(String txtSearch, String trangThai, Pageable pageable);

    KichCo update(UUID id, KichCoRequest kichCoRequest);

    KichCo delete(UUID id);


    KichCo getOne(UUID uuid);

    KichCo findByTen(String ten);

}
