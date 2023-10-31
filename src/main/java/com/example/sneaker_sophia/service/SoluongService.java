package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.GioHang;
import com.example.sneaker_sophia.entity.GioHangChiTiet;
import com.example.sneaker_sophia.entity.IdGioHangChiTiet;
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
    public void increaseQuantity(UUID gioHangId, UUID chiTietGiayId) {
        GioHang gioHang = gioHangRepository.findById(gioHangId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Giỏ hàng với ID: " + gioHangId));

        // Lấy thông tin ChiTietGiay và ChiTietGiayChiTiet từ cơ sở dữ liệu
        ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(chiTietGiayId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy ChiTietGiay với ID: " + chiTietGiayId));

        GioHangChiTiet gioHangChiTiet = gioHangChiTietRepository.findById_GioHangAndId_ChiTietGiay(gioHang, chiTietGiay);

        if (gioHangChiTiet != null) {
            int currentQuantity = gioHangChiTiet.getSoLuong();
            int chiTietGiayQuantity = chiTietGiay.getSoLuong();

            int newQuantity = currentQuantity + 1;

            if (newQuantity > chiTietGiayQuantity) {
                gioHangChiTiet.setSoLuong(chiTietGiayQuantity);
            } else {
                gioHangChiTiet.setSoLuong(newQuantity);
            }
        }
    }


    @Transactional
    public boolean decreaseQuantity(UUID gioHangId, UUID chiTietGiayId) {
        // Kiểm tra số lượng hiện có trong cơ sở dữ liệu
        int availableQuantity = gioHangChiTietRepository.getAvailableQuantity(gioHangId, chiTietGiayId);

        // Kiểm tra nếu số lượng giảm về 0 hoặc âm hơn, thì không cho phép giảm
        int decreaseBy = 1;
        if (availableQuantity > decreaseBy) {
            gioHangChiTietRepository.decreaseQuantity(gioHangId, chiTietGiayId);
            return true; // Số lượng giảm thành công
        } else {
            return false; // Số lượng đã là 0 hoặc âm, không thực hiện giảm
        }
    }

    private List<GioHangChiTiet> productList = new ArrayList<>();

    public List<GioHangChiTiet> getUpdatedProducts() {
        // Thực hiện logic để lấy dữ liệu sản phẩm mới, ví dụ:
        productList = this.gioHangChiTietRepository.findAll();

        return productList;
    }
}
