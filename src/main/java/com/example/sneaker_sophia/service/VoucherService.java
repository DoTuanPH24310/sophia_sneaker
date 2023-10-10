package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;





    public Page<Voucher> findAll(Pageable pageable){
        return  voucherRepository.findAll(pageable);
    }

    public Page<Voucher> findAll(Pageable pageable, Example e){
        return  voucherRepository.findAll(e,pageable);
    }



}
