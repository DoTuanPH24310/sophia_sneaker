package com.example.sneaker_sophia.config;
import com.example.sneaker_sophia.entity.TaiKhoan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> {
            // Lấy thông tin về người đăng nhập hiện tại từ SecurityContextHolder
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.empty();
            }


            return Optional.of(authentication.getName());
        };
    }

//    @Bean
//    public AuditorAware<String> auditorProvider() {
//        return () -> {
//            // Lấy thông tin về người đăng nhập hiện tại từ SecurityContextHolder
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            if (authentication == null || !authentication.isAuthenticated()) {
//                return Optional.empty();
//            }
//
//            // Trả về tên người đăng nhập
//            TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByEmail(authentication.getName());
//
//            // Kiểm tra xem taiKhoan có giá trị hay không
//            if (taiKhoan != null) {
//                String name = taiKhoan.getTen();
//                return Optional.of(name);
//            } else {
//                // Trong trường hợp taiKhoan là null, trả về Optional.empty()
//                return Optional.empty();
//            }
//        };
//    }
}