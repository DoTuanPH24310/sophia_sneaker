package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final LoginRepository loginRepository;
    private final GioHangRepository gioHangRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @Autowired
    public CartService(LoginRepository loginRepository, GioHangRepository gioHangRepository) {
        this.loginRepository = loginRepository;
        this.gioHangRepository = gioHangRepository;
    }

    public void addToCart(String userEmail, UUID chiTietGiayId) {
        if (!loginRepository.existsByEmail(userEmail)) {
            return;
        }
        TaiKhoan taiKhoan = loginRepository.findByEmail(userEmail);
        GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);

        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setTaiKhoan(taiKhoan);
            gioHang.setNgayTao(LocalDate.now());
            gioHang = gioHangRepository.save(gioHang);
        }

        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId).get();

        GioHangChiTiet cartItem = gioHangChiTietRepository.findById_GioHangAndId_ChiTietGiay(gioHang, chiTietGiay);
        if (cartItem == null) {
            cartItem = new GioHangChiTiet(new IdGioHangChiTiet(gioHang, chiTietGiay), 1);
            cartItem.setNgayTao(LocalDate.now());
        } else if (cartItem != null) {
            if (cartItem.getSoLuong() >= chiTietGiay.getSoLuong()) {
                cartItem.setSoLuong(chiTietGiay.getSoLuong());
            } else {
                cartItem.setSoLuong(cartItem.getSoLuong() + 1);
            }
            cartItem.setNgaySua(LocalDate.now());
        }
        gioHangChiTietRepository.save(cartItem);
    }

    public void addToCartNoLogin(UUID id, HttpSession httpSession) {
        Optional<ChiTietGiay> chiTietSanPham = this.chiTietGiayRepository.findById(id);
        CartItem item = new CartItem(chiTietSanPham.get().getId(),
                chiTietSanPham.get().getAnhs().get(0).getDuongDan(),
                chiTietSanPham.get().getGiay().getTen(),
                chiTietSanPham.get().getTen(),
                chiTietSanPham.get().getHang().getTen(),
                chiTietSanPham.get().getLoaiGiay().getTen(),
                chiTietSanPham.get().getMauSac().getTen(),
                1,
                chiTietSanPham.get().getGia());

        Cart cartSession = (Cart) httpSession.getAttribute("cart");
        if (cartSession == null) {
            Cart cart = new Cart();
            List<CartItem> list = new ArrayList<>();
            list.add(item);
            cart.setItems(list);
            httpSession.setAttribute("cart", cart);
        } else {
            Cart cart = (Cart) httpSession.getAttribute("cart");
            List<CartItem> listItem = cart.getItems();

            boolean itemExists = false;
            for (CartItem itemTmp : listItem) {
                if (itemTmp.getId().equals(id)) {
                    itemTmp.setSoLuong(itemTmp.getSoLuong() + 1);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                listItem.add(item);
            }

            // Cập nhật giỏ hàng trong session
            httpSession.setAttribute("cart", cart);
        }
    }

    @Transactional
    public void mergeCarts(String userEmail, Cart cartSession) {
        if (!loginRepository.existsByEmail(userEmail)) {
            return;
        }
        TaiKhoan taiKhoan = loginRepository.findByEmail(userEmail);
        GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);

        if (gioHang == null) {
            gioHang = new GioHang();
            gioHang.setTaiKhoan(taiKhoan);
            gioHang.setNgayTao(LocalDate.now());
            gioHang = gioHangRepository.save(gioHang);
        }

        for (CartItem cartItem : cartSession.getItems()) {
            ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(cartItem.getId()).orElse(null);
            if (chiTietGiay != null) {
                GioHangChiTiet cartItemDatabase = gioHangChiTietRepository.findById_GioHangAndId_ChiTietGiay(gioHang, chiTietGiay);
                if (cartItemDatabase == null) {
                    cartItemDatabase = new GioHangChiTiet(new IdGioHangChiTiet(gioHang, chiTietGiay), cartItem.getSoLuong());
                    cartItemDatabase.setNgayTao(LocalDate.now());
                } else {
                    cartItemDatabase.setSoLuong(cartItemDatabase.getSoLuong() + cartItem.getSoLuong());
                    cartItemDatabase.setNgaySua(LocalDate.now());
                }
                gioHangChiTietRepository.save(cartItemDatabase);
            }
        }
    }

    public Cart getOrCreateCartFromSession(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            // Nếu giỏ hàng không tồn tại trong session, tạo một giỏ hàng mới
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        return cart;
    }

    public List<GioHangChiTiet> getCartItems(String userEmail) {
        TaiKhoan taiKhoan = loginRepository.findByEmail(userEmail);
        if (taiKhoan != null) {
            GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);
            if (gioHang != null) {
                return gioHangChiTietRepository.findById_GioHang(gioHang);
            }
        }
        return new ArrayList<>();
    }

    public void removeFromCart(UUID gioHangId, UUID chiTietGiayId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        // Truy vấn ChiTietGiay từ chiTietGiayId
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Chi tiết giày với ID: " + chiTietGiayId));

        IdGioHangChiTiet id = new IdGioHangChiTiet(gioHang, chiTietGiay);
        gioHangChiTietRepository.deleteById(id);
    }

    public long countCartItems(String userEmail) {
        return gioHangChiTietRepository.countProduct(userEmail);
    }




}



