package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.service.HoaDonChiTietDTService;
import com.example.sneaker_sophia.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/admin/")
public class ThongKeController {
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    HoaDonChiTietDTService hoaDonChiTietDTService;
    @GetMapping("thong-ke")
    public String index(Model model,
                        @RequestParam(name = "ngayBatDau", required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayBatDau,
                        @RequestParam(name = "ngayKetThuc", required = false)
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime ngayKetThuc) {

        // Giữ giá trị của ngayBatDau và ngayKetThuc trong model để truyền lại cho view
        model.addAttribute("ngayBatDau", ngayBatDau);
        model.addAttribute("ngayKetThuc", ngayKetThuc);

        // Các logic xử lý khác
//        List<Object[]> revenueDataList = hoaDonService.getDoanhThuTheoThang(ngayBatDau,ngayKetThuc);
//        model.addAttribute("listHDCT",hoaDonChiTietDTService.findAll());
        model.addAttribute("soHoaDon", hoaDonService.countHoaDonByDateRange(ngayBatDau, ngayKetThuc));
        model.addAttribute("doanhThu", hoaDonService.calculateTongTienByDate(ngayBatDau, ngayKetThuc));

        return "admin/thongKe/thongKe";
    }

}
