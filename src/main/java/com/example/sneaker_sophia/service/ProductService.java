package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ChiTietGiay> filterProducts(String hang, Double gia, String loai, String deGiay, String kichCo, String mauSac) {
        List<ChiTietGiay> filteredProducts = new ArrayList<>();

        if (hang != null) {
            filteredProducts.addAll(productRepository.findByHang_Ten(hang));
        }
        if (gia != null) {
            filteredProducts.addAll(productRepository.findByGiaLessThanEqual(gia));
        }
        if (loai != null) {
            filteredProducts.addAll(productRepository.findByLoaiGiay_Ten(loai));
        }
        if (deGiay != null) {
            filteredProducts.addAll(productRepository.findByDeGiay_Ten(deGiay));
        }
        if (kichCo != null) {
            filteredProducts.addAll(productRepository.findByKichCo_Ten(kichCo));
        }
        if (mauSac != null) {
            filteredProducts.addAll(productRepository.findByMauSac_Ten(mauSac));
        }

        // Loại bỏ các sản phẩm trùng lặp trong danh sách lọc
        Set<ChiTietGiay> uniqueProducts = new HashSet<>(filteredProducts);

        // Chuyển danh sách lọc thành một danh sách mới
        filteredProducts = new ArrayList<>(uniqueProducts);

        return filteredProducts;
    }


    public List<ChiTietGiay> getTop10BestSelling() {
        List<Object[]> results = productRepository.findTop10BestSelling();
        List<ChiTietGiay> topSellingChiTietGiayList = new ArrayList<>();

        for (Object[] result : results) {
            ChiTietGiay chiTietGiay = (ChiTietGiay) result[0];
            topSellingChiTietGiayList.add(chiTietGiay);
        }

        return topSellingChiTietGiayList;
    }
}
