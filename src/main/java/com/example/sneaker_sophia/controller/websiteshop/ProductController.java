package com.example.sneaker_sophia.controller.websiteshop;


import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService; // ProductService là một service xử lý dữ liệu sản phẩm

    @GetMapping("/filter")
    public List<ChiTietGiay> filterProducts(
            @RequestParam(name = "hang", required = false) String hang,
            @RequestParam(name = "gia", required = false) Double gia,
            @RequestParam(name = "loai", required = false) String loai,
            @RequestParam(name = "deGiay", required = false) String deGiay,
            @RequestParam(name = "kichCo", required = false) String kichCo,
            @RequestParam(name = "mauSac", required = false) String mauSac
    ) {
        // Gọi phương thức của ProductService để lọc sản phẩm dựa trên các tham số truy vấn
        List<ChiTietGiay> filteredProducts = productService.filterProducts(hang, gia, loai, deGiay, kichCo, mauSac);
        return filteredProducts;
    }
}

