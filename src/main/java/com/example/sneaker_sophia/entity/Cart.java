package com.example.sneaker_sophia.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        items.add(item);
    }

    public void increaseQuantity(UUID productId) {
        for (CartItem item : items) {
            if (item.getId().equals(productId)) {
                item.setSoLuong(item.getSoLuong() + 1);
                break;
            }
        }
    }

    public void decreaseQuantity(UUID productId) {
        for (CartItem item : items) {
            if (item.getId().equals(productId)) {
                if (item.getSoLuong() > 1) {
                    item.setSoLuong(item.getSoLuong() - 1);
                }
                break;
            }
        }
    }

    public void updateQuantity(UUID productId, int quantity) {
        for (CartItem item : items) {
            if (item.getId().equals(productId)) {
                item.setSoLuong(quantity);
                break;
            }
        }
    }


    public void removeItem(UUID productId) {
        items.removeIf(item -> item.getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }


}

