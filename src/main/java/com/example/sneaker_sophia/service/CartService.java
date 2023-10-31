package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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
        } else {
            cartItem.setSoLuong(cartItem.getSoLuong() + 1);
            cartItem.setNgaySua(LocalDate.now());
        }
        gioHangChiTietRepository.save(cartItem);
    }

    public List<GioHangChiTiet> getCartItems(String userEmail) {
        TaiKhoan taiKhoan = loginRepository.findByEmail(userEmail);
        if (taiKhoan != null) {
            GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);
            if (gioHang != null) {
                return gioHangChiTietRepository.findById_GioHang(gioHang);
            }
        }
        return new ArrayList<>(); // hoặc bạn có thể trả về null hoặc xử lý khác tùy theo trường hợp của bạn
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

    public void updateCartItemQuantity(UUID gioHangId, UUID chiTietGiayId, Integer newQuantity) {
        // Truy vấn GioHang từ gioHangId
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        // Truy vấn ChiTietGiay từ chiTietGiayId
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Chi tiết giày với ID: " + chiTietGiayId));

        // Tạo đối tượng IdGioHangChiTiet
        IdGioHangChiTiet id = new IdGioHangChiTiet(gioHang, chiTietGiay);

        Optional<GioHangChiTiet> cartItemOptional = gioHangChiTietRepository.findById(id);
        if (cartItemOptional.isPresent()) {
            GioHangChiTiet cartItem = cartItemOptional.get();
            cartItem.setSoLuong(newQuantity);
            gioHangChiTietRepository.save(cartItem);
        } else {
            // Xử lý khi không tìm thấy sản phẩm trong giỏ hàng chi tiết
            throw new EntityNotFoundException("Không tìm thấy sản phẩm trong giỏ hàng chi tiết với ID: " + id);
        }
    }




}



