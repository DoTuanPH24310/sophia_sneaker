package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.KMRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service("kmService")
public class KMService {

    @Resource(name = "kmRepository")
    KMRepository kmRepository;

    public List<Voucher> getAllKMByIdctg(UUID idctg){
        return kmRepository.getAllKMByIdctg(idctg);
    }

    public List<Voucher> getAllKMByIdctgHH(UUID idctg){
        return kmRepository.getAllKMByIdctgHH(idctg);
    }

    public Voucher getKMByIdctg(UUID idctg){
        return kmRepository.getKMByIdctg(idctg);
    }

    public void saveVC(Voucher voucher){
        kmRepository.save(voucher);
    }
}