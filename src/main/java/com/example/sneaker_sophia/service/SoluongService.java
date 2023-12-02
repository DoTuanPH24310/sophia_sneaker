package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GioHangChiTietRepository;
import com.example.sneaker_sophia.repository.GioHangRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SoluongService {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;


    @Transactional
    public String increaseQuantity(UUID gioHangId, UUID chiTietGiayId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ChiTietGiay với ID: " + chiTietGiayId));

        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById_GioHangAndId_ChiTietGiay(gioHang, chiTietGiay);

        if (gioHangChiTiet != null) {
            int currentQuantity = gioHangChiTiet.getSoLuong();
            int chiTietGiayQuantity = chiTietGiay.getSoLuong();

            int newQuantity = currentQuantity + 1;

            if (newQuantity > chiTietGiayQuantity) {
                // Trả về thông báo lỗi nếu số lượng vượt quá giới hạn
                return "Số lượng sản phẩm không đủ!";
            } else {
                // Tiến hành tăng số lượng nếu hợp lệ
                gioHangChiTiet.setSoLuong(newQuantity);
                return "success";
            }
        } else {
            // Trả về thông báo lỗi nếu không tìm thấy chi tiết giày trong giỏ hàng
            return "Không tìm thấy chi tiết giày trong giỏ hàng!";
        }
    }

    public boolean isValidQuantity(UUID chiTietGiayId, int newQuantity) {
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ChiTietGiay với ID: " + chiTietGiayId));

        int chiTietGiayQuantity = chiTietGiay.getSoLuong();

        // Kiểm tra nếu newQuantity lớn hơn số lượng sản phẩm thì trả về false
        return newQuantity >= 1 && newQuantity <= chiTietGiayQuantity;
    }


    @Transactional
    public boolean decreaseQuantity(UUID gioHangId, UUID chiTietGiayId) {
        int availableQuantity = gioHangChiTietRepository.getAvailableQuantity(gioHangId, chiTietGiayId);

        int decreaseBy = 1;
        if (availableQuantity > decreaseBy) {
            gioHangChiTietRepository.decreaseQuantity(gioHangId, chiTietGiayId);
            return true;
        } else {
            return false;
        }
    }

    private List<GioHangChiTiet> productList = new ArrayList<>();

    public List<GioHangChiTiet> getUpdatedProducts() {
        productList = this.gioHangChiTietRepository.findAll();

        return productList;
    }

    public void updateQuantity(UUID gioHangId, UUID chiTietGiayId, int newQuantity) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ChiTietGiay với ID: " + chiTietGiayId));

        IdGioHangChiTiet id = new IdGioHangChiTiet();
        id.setGioHang(gioHang);
        id.setChiTietGiay(chiTietGiay);

        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy GioHangChiTiet với ID: " + id));

        if (gioHangChiTiet != null) {
            int chiTietGiayQuantity = chiTietGiay.getSoLuong();

            if (newQuantity < 1) {
                newQuantity = 1;
            }

            if (newQuantity > chiTietGiayQuantity) {
                gioHangChiTiet.setSoLuong(chiTietGiayQuantity);
            } else {
                gioHangChiTiet.setSoLuong(newQuantity);
            }

            if (gioHangChiTietRepository != null) {
                gioHangChiTietRepository.save(gioHangChiTiet);
            } else {
                System.out.println("gioHangChiTietRepository is null");
            }
        }
    }


    @Transactional
    public void removeAllItems(TaiKhoan taiKhoan) {
        GioHang gioHang = gioHangRepository.findByTaiKhoan(taiKhoan);
        if (gioHang != null) {
            gioHang.getGioHangChiTiets().clear();
            entityManager.createQuery("DELETE FROM GioHangChiTiet g WHERE g.id.gioHang = :gioHang")
                    .setParameter("gioHang", gioHang)
                    .executeUpdate();
        }
    }




}
