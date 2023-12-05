package com.example.sneaker_sophia.controller.controllerAdvice;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.entity.CartItem;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.repository.CartRepository;
import com.example.sneaker_sophia.service.GioHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    GioHangService gioHangService;

    @ModelAttribute("soLuongSessionGioHang")
    public long getCartItemCount(Authentication authentication) {
        HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Cart cartSession = (Cart) httpSession.getAttribute("cart");
        long cartItemCount = Objects.requireNonNullElse(cartSession, new Cart()).getItems().stream().mapToLong(CartItem::getSoLuong).count();

        if (authentication != null && authentication.isAuthenticated()) {
            // Kiểm tra vai trò VT003 trước khi gọi hàm
            boolean hasRoleVT003 = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("VT003"));

            if (hasRoleVT003) {
                GioHang gioHang = this.gioHangService.getCartByEmail(authentication.getName());
                List<GioHangChiTiet> cartItemsDatabase = gioHangService.getCartItems(gioHang.getId());
                cartItemCount += cartItemsDatabase != null ? cartItemsDatabase.stream().mapToLong(GioHangChiTiet::getSoLuong).count() : 0;
            }
        }

        return cartItemCount;
    }

}
