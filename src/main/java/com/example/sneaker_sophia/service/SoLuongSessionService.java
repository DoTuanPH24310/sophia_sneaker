package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Cart;
import com.example.sneaker_sophia.entity.CartItem;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SoLuongSessionService {

    @SuppressWarnings("unchecked")
    public void increaseQuantity(UUID chiTietGiayId, HttpSession session) {
        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);

        if (cartItem != null) {
            cartItem.setSoLuong(cartItem.getSoLuong() + 1);
        }

        updateCartInSession(cart, session);
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
        Cart cart = getCartFromSession(session);
        CartItem cartItem = findCartItemById(cart, chiTietGiayId);

        if (cartItem != null) {
            cartItem.setSoLuong(newQuantity);
        }

        updateCartInSession(cart, session);
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

}
