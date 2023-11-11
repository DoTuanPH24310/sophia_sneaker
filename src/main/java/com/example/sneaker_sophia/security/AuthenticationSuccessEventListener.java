package com.example.sneaker_sophia.security;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private CartService cartService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Object details = event.getAuthentication().getDetails();

        if (details instanceof WebAuthenticationDetails) {
            // Lấy thông tin từ WebAuthenticationDetails
            WebAuthenticationDetails webDetails = (WebAuthenticationDetails) details;
            String remoteAddress = webDetails.getRemoteAddress();
            // Có thể sử dụng thông tin này nếu cần

            // Kiểm tra xem người dùng đã đăng nhập hay không
            String username = event.getAuthentication().getName();
            if (!"anonymousUser".equals(username)) {
                // Người dùng đã đăng nhập
                // Lấy đối tượng HttpServletRequest từ RequestContextHolder
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

                // Lấy đối tượng HttpSession từ HttpServletRequest
                HttpSession session = request.getSession();

                // Lấy giỏ hàng từ session hoặc tạo mới nếu chưa có
                Cart cart = cartService.getOrCreateCartFromSession(session);

                // Kiểm tra xem giỏ hàng session có dữ liệu hay không
                if (cart != null && !cart.getItems().isEmpty()) {
                    // Thêm sản phẩm từ giỏ hàng session vào giỏ hàng database
                    cartService.mergeCarts(username, cart);

                    // Xóa giỏ hàng session sau khi đã thêm vào giỏ hàng database
                    cart.clear();
                    session.setAttribute("cart", cart);
                }
            }
        }

        // Các xử lý khác...
    }
}
