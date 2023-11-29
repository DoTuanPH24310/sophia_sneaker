package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.service.HoaDonChiTietDTService;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chart")
public class ChartRestController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    HoaDonChiTietDTService hoaDonChiTietDTService;
    @Autowired
    HoaDonWebRepository hoaDonWebRepository;
    @Autowired
    HoaDonChiTietServive hoaDonChiTietServive;

    @GetMapping("/revenue")
    public List<Object[]> getRevenueChartData(@RequestParam(name = "ngayBatDau", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
                                              @RequestParam(name = "ngayKetThuc", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc,
                                              Model model) {
        // Trừ đi một năm từ endDate
        LocalDateTime endDateMinusOneYear = ngayKetThuc.minus(1, ChronoUnit.YEARS);
        LocalDateTime startDateMinusOneYear = ngayBatDau.minus(1, ChronoUnit.YEARS);

        // Gọi truy vấn với tham số endDateMinusOneYear
        List<Object[]> result = hoaDonService.getDoanhThuTheoThang(ngayBatDau, ngayKetThuc,  hoaDonWebRepository.determineTimeUnit(ngayBatDau, ngayKetThuc), endDateMinusOneYear,startDateMinusOneYear);
        model.addAttribute("listHDCT", hoaDonChiTietDTService.findAll());
        return result;
    }

    @GetMapping("/product")
    public List<Object[]> getProductChartData(@RequestParam(name = "ngayBatDau", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
                                              @RequestParam(name = "ngayKetThuc", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc,
                                              Model model) {
        List<Object[]> productDataList = hoaDonChiTietDTService.findTop10BestSellingProductsByNam(ngayBatDau, ngayKetThuc, hoaDonWebRepository.determineTimeUnit(ngayBatDau, ngayKetThuc));
        model.addAttribute("listHDCT", hoaDonChiTietDTService.findAll());
        return productDataList;
    }

    @GetMapping("/status")
    public List<Object[]> getStatusChartData(@RequestParam(name = "ngayBatDau", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
                                              @RequestParam(name = "ngayKetThuc", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc,
                                              Model model) {
        List<Object[]> statusDataList = hoaDonService.countHoaDonByDateAndStatus(ngayBatDau, ngayKetThuc);
        model.addAttribute("listHDCT", hoaDonChiTietDTService.findAll());
        return statusDataList;
    }
    @GetMapping("/thong-ke")
    public Map<String, Object> thongKe(
            @RequestParam(name = "ngayBatDau", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
            @RequestParam(name = "ngayKetThuc", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc) {


        Map<String, Object> result = Map.of(
                "doanhThu", hoaDonService.calculateTongTienByDate(ngayBatDau, ngayKetThuc),
                "soHoaDon", hoaDonService.countHoaDonByDateRange(ngayBatDau, ngayKetThuc),
                "soSanPham", hoaDonChiTietServive.sumSoLuongByHoaDonTrangThaiEquals1()
        );
        return result;
    }

}