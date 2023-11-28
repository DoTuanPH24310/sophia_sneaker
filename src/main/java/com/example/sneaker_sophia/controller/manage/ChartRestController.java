package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.service.HoaDonChiTietDTService;
import com.example.sneaker_sophia.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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

    @GetMapping("/revenue")
    public List<Object[]> getRevenueChartData(@RequestParam(name = "ngayBatDau", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
                                              @RequestParam(name = "ngayKetThuc", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc,
                                              Model model) {
        List<Object[]> revenueDataList = hoaDonService.getDoanhThuTheoThang(ngayBatDau, ngayKetThuc, hoaDonWebRepository.determineTimeUnit(ngayBatDau, ngayKetThuc));
        System.out.println("nít" + hoaDonWebRepository.determineTimeUnit(ngayBatDau, ngayKetThuc));
        model.addAttribute("listHDCT", hoaDonChiTietDTService.findAll());
        return revenueDataList;
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
        List<Object[]> statusDataList = hoaDonService.countHoaDonByTrangThai(ngayBatDau, ngayKetThuc);
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
                "ngayBatDau", ngayBatDau,
                "ngayKetThuc", ngayKetThuc,
                "soHoaDon", hoaDonService.countHoaDonByDateRange(ngayBatDau, ngayKetThuc),
                "soHoaDonThanhCong", hoaDonService.countHoaDonTrangThaiThanhCongByDate(ngayBatDau, ngayKetThuc),
                "soHoaDonHuy", hoaDonService.countHoaDonTrangThaiHuyByDate(ngayBatDau, ngayKetThuc),
                "doanhThu", hoaDonService.calculateTongTienByDate(ngayBatDau, ngayKetThuc)
        );
        return result;
    }

}