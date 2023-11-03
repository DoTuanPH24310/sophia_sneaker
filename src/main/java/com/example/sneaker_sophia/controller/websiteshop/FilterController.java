package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.ChiTietGiayDTO;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FilterController {
    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @GetMapping("/filterChiTietGiay")
    public List<ChiTietGiayDTO> filterChiTietGiay(
            @RequestParam(value = "giay", required = false) List<String> giayTen,
            @RequestParam(value = "kichCo", required = false) List<String> kichCoTen,
            @RequestParam(value = "deGiay", required = false) List<String> deGiayTen,
            @RequestParam(value = "hang", required = false) List<String> hangTen,
            @RequestParam(value = "loaiGiay", required = false) List<String> loaiGiayTen,
            @RequestParam(value = "mauSac", required = false) List<String> mauSacTen
    ) {

        List<ChiTietGiay> filteredChiTietGiay = chiTietGiayService.filterChiTietGiay(giayTen, kichCoTen, deGiayTen,hangTen, loaiGiayTen, mauSacTen);

        // Chuyển đổi danh sách sản phẩm sang DTO
        List<ChiTietGiayDTO> dtos = convertToDTOs(filteredChiTietGiay);
        System.out.println("dtos đây : "+dtos.size());
        System.out.println(giayTen);
        System.out.println(kichCoTen);
        System.out.println(deGiayTen);
        System.out.println(loaiGiayTen);
        System.out.println(hangTen);
        System.out.println(mauSacTen);
        return dtos;
    }

    // Phương thức để chuyển đổi từ ChiTietGiay sang ChiTietGiayDTO
    private List<ChiTietGiayDTO> convertToDTOs(List<ChiTietGiay> chiTietGiayList) {
        return chiTietGiayList.stream()
                .map(chiTietGiay -> {
                    ChiTietGiayDTO dto = new ChiTietGiayDTO();
                    dto.setId(chiTietGiay.getId());
                    dto.setTen(chiTietGiay.getHang().getTen());
                    dto.setGia(chiTietGiay.getGia());
                    dto.setMa(chiTietGiay.getMa());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
