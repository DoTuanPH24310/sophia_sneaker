package com.example.sneaker_sophia.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityUtils {
    public static List<String> getAuthorities() {
        List<String> reList = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities() != null) {
            List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                reList.add(authority.getAuthority());
            }
        }
        return reList;
    }

}
