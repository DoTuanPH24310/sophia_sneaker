package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.MauSacRequest;
import com.example.sneaker_sophia.entity.MauSac;
import com.example.sneaker_sophia.repository.MauSacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface MauSacService {
    List<MauSac> getAll();

    MauSac add(MauSacRequest mauSacRequest);

    Optional<MauSac> findOne(UUID id);

    Page<MauSac> fillter(String txtSearch, String trangThai, Pageable pageable);

    MauSac update(UUID id, MauSacRequest mauSacRequest);

    MauSac delete(UUID id);
}
