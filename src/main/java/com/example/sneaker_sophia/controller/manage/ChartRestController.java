package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Autowired
    LSHDService lshdService;
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @GetMapping("/year")
    public List<Object[]> getRevenueChartDataYear() {
        // Gọi truy vấn với tham số endDateMinusOneYear
        List<Object[]> getStatisticsByMonth = hoaDonWebRepository.getSumTongTienByYear();
        return getStatisticsByMonth;
    }
    @GetMapping("/month")
    public List<Object[]> getRevenueChartDataMonth(@RequestParam(name = "thongKeDoanhThu", required = false) LocalDateTime thongKeDoanhThu) {
        // Gọi truy vấn với tham số endDateMinusOneYear
        List<Object[]> getStatisticsByMonth = hoaDonWebRepository.getStatisticsByMonth(thongKeDoanhThu);
        return getStatisticsByMonth;
    }

    @GetMapping("/day")
    public List<Object[]> getRevenueChartDataDay(@RequestParam(name = "thongKeDoanhThu", required = false) LocalDateTime thongKeDoanhThu) {
        // Gọi truy vấn với tham số endDateMinusOneYear
        List<Object[]> getStatisticsByDay = hoaDonWebRepository.getStatisticsByDay(thongKeDoanhThu);

        return getStatisticsByDay;
    }
    @GetMapping("/hour")
    public List<Object[]> getRevenueChartDataHour(@RequestParam(name = "thongKeDoanhThu", required = false) LocalDateTime thongKeDoanhThu) {
        // Gọi truy vấn với tham số endDateMinusOneYear
        List<Object[]> getStatisticsByHour = hoaDonWebRepository.getStatisticsByHour(thongKeDoanhThu);

        return getStatisticsByHour;
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

        LocalDateTime ngayHienTai = LocalDateTime.now();
        LocalDateTime ngayHienTaiStart = ngayHienTai.with(LocalTime.MIN); // Lấy thời điểm bắt đầu ngày hiện tại
        LocalDateTime ngayHomQuaStart = ngayHienTai.minusDays(1).with(LocalTime.MIN); // Lấy thời điểm bắt đầu ngày hôm qua
        LocalDateTime ngayHomQuaHienTai = ngayHienTaiStart.minusSeconds(1); // Lấy thời điểm cuối cùng của ngày hôm qua

        Double sumHomQua = hoaDonWebRepository.sumTongTienByNgayHomQua(ngayHomQuaStart, ngayHomQuaHienTai);
        Double sumHomNay = hoaDonWebRepository.sumTongTienByNgayHienTai();

        LocalDateTime ngayDauThangThangTruoc = ngayHienTaiStart.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()); // Lấy ngày đầu tiên của tháng trước
        LocalDateTime ngayHienTaiEnd = ngayHienTai.with(LocalTime.MAX);

// Kiểm tra nếu tháng là 1 thì gán giá trị là 12
        if (ngayHienTaiEnd.getMonthValue() == 1) {
            ngayHienTaiEnd = ngayHienTaiEnd.withMonth(12);
        } else {
            // Ngược lại, giảm tháng đi một đơn vị
            ngayHienTaiEnd = ngayHienTaiEnd.minusMonths(1);
        }

        System.out.println("Ngày cuối cùng của tháng trước: " + ngayHienTaiEnd);
        System.out.println(ngayDauThangThangTruoc);
        Double sumThangNay = hoaDonWebRepository.sumTongTienByThangHienTai();
        Double sumThangTruoc = hoaDonWebRepository.sumTongTienTuDauThangDenNgayHienTaiThangTruoc(ngayDauThangThangTruoc, ngayHienTaiEnd);
        System.out.println("á"+sumThangTruoc);
        Double phanTramTangGiam = sumHomQua == 0 ? 0 : ((sumHomNay - sumHomQua) / sumHomQua) * 100;
        Double phanTramTangGiamThang = sumThangNay == 0 ? 0 : ((sumThangNay - sumThangTruoc) / sumThangTruoc) * 100;
        DecimalFormat df = new DecimalFormat("#.##");
        String formattedPhanTramTangGiam = df.format(phanTramTangGiam);
        String formattedPhanTramTangGiamThang = df.format(phanTramTangGiamThang);

        Map<String, Object> result = Map.of(
                "doanhThu", hoaDonService.calculateTongTienByDate(ngayBatDau, ngayKetThuc),
                "soHoaDon", hoaDonService.countHoaDonByDateRange(ngayBatDau, ngayKetThuc),
                "soSanPham", hoaDonChiTietServive.sumSoLuongByHoaDonTrangThaiEquals1(),
                "soDonOnline",lshdService.countByHoaDonTrangThaiAndPhuongThuc(ngayBatDau, ngayKetThuc),
                "doanhThuWebsite",lshdService.sumTongTienByHoaDonTrangThaiAndPhuongThuc(ngayBatDau, ngayKetThuc),
                "phamTramTangGiamNgay",formattedPhanTramTangGiam,
                "phamTramTangGiamThang",formattedPhanTramTangGiamThang
        );
        return result;
    }

    @GetMapping("/san-pham-sap-het")
    public Map<String, Object> sanPhamSapHet(
            @RequestParam(name = "soLuongDoi", required = false) int soLuongDoi) {
        Map<String, Object> result = Map.of(
                "sanPhamSapHet",chiTietGiayService.getConcatenatedInfoAndSoLuongBySoLuong(soLuongDoi));
        return result;
    }

}