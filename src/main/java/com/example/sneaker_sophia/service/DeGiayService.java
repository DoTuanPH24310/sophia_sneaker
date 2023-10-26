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

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;

import java.util.List;

@Service
public interface DeGiayService {
    List<DeGiay> getAll();

    DeGiay add(DeGiayRequest deGiayRequest);

    Optional<DeGiay> findOne(UUID id);

    Page<DeGiay> fillter(String txtSearch, String trangThai, Pageable pageable);

    DeGiay update(UUID id, DeGiayRequest deGiayRequest);

    DeGiay delete(UUID id);
}
