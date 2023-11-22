package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.HangRequest;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.HangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface HangService {
    List<Hang> getAll();

    Hang add(HangRequest hangRequest);

    Optional<Hang> findOne(UUID id);

    Page<Hang> fillter(String txtSearch, String trangThai, Pageable pageable);

    Hang update(UUID id, HangRequest hangRequest);

    Hang delete(UUID id);

    Hang getOne(UUID uuid);

    Hang findByTen(String ten);

    List<Hang> findByTrangThaiEquals(Integer i);
}
