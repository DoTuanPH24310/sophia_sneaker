package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.entity.CartItem;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.validate.AlertInfo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SoLuongSessionService {
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    private KhuyenMaiWebService khuyenMaiWebService;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    public Integer getProductQuantity(UUID chiTietGiayId) {
        return chiTietGiayRepository.getProductQuantityById(chiTietGiayId);
    }

    @SuppressWarnings("unchecked")
    public boolean canIncreaseQuantity(UUID chiTietGiayId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId).orElse(null);

        if (chiTietGiay != null && cartItem != null) {
            // Kiểm tra xem số lượng giỏ hàng và số lượng sản phẩm có thỏa mãn không
            return cartItem.getSoLuong() + 1 <= chiTietGiay.getSoLuong();
        }

        return false;
    }


    @SuppressWarnings("unchecked")
    public void increaseQuantity(UUID chiTietGiayId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId).orElse(null);

        if (chiTietGiay != null && cartItem != null) {
            if (cartItem.getSoLuong() + 1 <= chiTietGiay.getSoLuong()) {
                cartItem.setSoLuong(cartItem.getSoLuong() + 1);
                updateCartInSession(cart, session);
            } else {
                // Thông báo khi vượt quá số lượng
                throw new RuntimeException("Số lượng giỏ hàng vượt quá số lượng sản phẩm.");
            }
        }
    }


    @SuppressWarnings("unchecked")
    public void decreaseQuantity(UUID chiTietGiayId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);

        if (cartItem != null && cartItem.getSoLuong() > 1) {
            cartItem.setSoLuong(cartItem.getSoLuong() - 1);
        }

        updateCartInSession(cart, session);
    }

    @SuppressWarnings("unchecked")
    public void updateQuantity(UUID chiTietGiayId, int newQuantity, HttpSession session) {
        if (newQuantity < 1) {
            newQuantity = 1;
            alertInfo.alert("errOnline", "Số lượng không hợp lệ");

        }

        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);
        ChiTietGiay chiTietGiay = this.chiTietGiayRepository.findById(chiTietGiayId).orElse(null);
        if (chiTietGiay != null) {
            // Kiểm tra xem newQuantity có vượt quá số lượng của chi tiết giày không
            if (newQuantity > chiTietGiay.getSoLuong()) {
                newQuantity = chiTietGiay.getSoLuong();
            }

            if (cartItem != null) {
                cartItem.setSoLuong(newQuantity);
                updateCartInSession(cart, session);
            }
        }
    }


    private Cart getCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    private CartItem findCartItemById(Cart cart, UUID chiTietGiayId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(chiTietGiayId))
                .findFirst()
                .orElse(null);
    }

    private void updateCartInSession(Cart cart, HttpSession session) {
        session.setAttribute("cart", cart);
    }


    @SuppressWarnings("unchecked")
    public void removeItem(UUID chiTietGiayId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        List<CartItem> cartItems = cart.getItems();

        cartItems.removeIf(item -> item.getId().equals(chiTietGiayId));

        updateCartInSession(cart, session);
    }


    public void removeAllItems(HttpSession session) {
        Cart cart = getCartFromSession(session);

        if (cart == null) {
            throw new EntityNotFoundException("Cart not found in session");
        }
        cart.clear();
        updateCartInSession(cart,session);
    }

}
