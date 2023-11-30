package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public List<DiaChi> findByTaiKhoan_Email(String tenTaiKhoan) {
        return accountRepository.findByTaiKhoan_Email(tenTaiKhoan);
    }
}
