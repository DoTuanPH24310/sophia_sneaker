package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(username);
        if (taiKhoan == null) {
            throw new UsernameNotFoundException("User name not found");
        } else {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(taiKhoan.getIdVaiTro().getTen()));
            return new org.springframework.security.core.userdetails.User(
                    taiKhoan.getEmail(), taiKhoan.getMatKhau(), authorities);
        }
    }



}
