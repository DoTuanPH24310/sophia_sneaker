package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.DiaChiLoGin;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ThanhToanService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;

    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @Autowired
    private DiaChiTamChu diaChiTamChu;

    @Autowired
    private HoaDonChiTietWebRepository hoaDonChiTietRepository;

    @Autowired
    private HinhThucThanhToanWebRepository hinhThucThanhToanRepository;

    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;

    public void thucHienThanhToan(String email, List<GioHangChiTiet> cartItems, Integer hinhThucThanhToan,String diaChi, String tinh, String huyen, String xa) {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
        double total = 0.0;
        for (GioHangChiTiet cartItem : cartItems) {
            total += cartItem.getId().getChiTietGiay().getGia() * cartItem.getSoLuong();
        }
        double tongTienDonHang = 0.0;
        int tongSoLuongGiam = 0;
        int phanTramGiam = 0;
        Integer tongGiamGia = 0;
        int soLuongGiam = 0;
        double tongTienGiam = 0.0;
        int soHD = this.hoaDonRepository.soHD() + 1;
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDOn("HD" + soHD);
        hoaDon.setTaiKhoan(taiKhoan);
        hoaDon.setLoaiHoaDon(3);
        hoaDon.setTenKhachHang(taiKhoan.getTen());
        hoaDon.setSoDienThoai(taiKhoan.getSdt());
        hoaDon.setDiaChi(diaChi + "-" + xa + "-" + huyen + "-" + tinh);
        hoaDon.setPhiShip(20000.0);
        hoaDon.setTrangThai(3);
        hoaDon.setTongTien(0.0);
        hoaDon.setTienThua(0.0);
        hoaDon.setTongTien(tongTienDonHang);
        hoaDon.setKhuyenMai(tongTienGiam);
        HoaDon savedHoaDon = hoaDonWebRepository.save(hoaDon);
        for (GioHangChiTiet cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();

            int soLuongMua = cartItem.getSoLuong();
            int soLuongHienTai = chiTietGiay.getSoLuong();

            int soLuongPhieuGiamDaSuDung = 0;
            if (soLuongHienTai >= soLuongMua) {
                List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();
                // Tính toán số lượng giảm và giảm giá tương ứng
                for (CTG_KhuyenMai ctg : listCTG_KM) {
                    // Kiểm tra trạng thái giảm giá
                    if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                        if (ctg.getId().getVoucher() != null) {
                            soLuongGiam = ctg.getId().getVoucher().getSoLuong();
                        }
                        int soLuongGiamApDung = Math.min(soLuongGiam - soLuongPhieuGiamDaSuDung, soLuongMua);

                        // Cập nhật số lượng giảm giá của sản phẩm
                        tongSoLuongGiam = soLuongGiamApDung;
                        // Cập nhật giảm giá của sản phẩm
                        phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                        int giamGia = phanTramGiam * soLuongGiam;
                        tongGiamGia = giamGia;
                        // Cập nhật tổng số tiền giảm
                        double donGia = chiTietGiay.getGia();
                        int giam = ctg.getId().getVoucher().getPhanTramGiam();
                        double tienGiam = donGia * giam / 100 * soLuongGiamApDung;
                        tongTienGiam += tienGiam;
                        tongTienDonHang = total - tongTienGiam;
                        soLuongPhieuGiamDaSuDung += soLuongGiamApDung;

                        // Trừ đi số lượng đã sử dụng từ bảng giảm giá
                        ctg.getId().getVoucher().setSoLuong(ctg.getId().getVoucher().getSoLuong() - soLuongGiamApDung);
                        voucherRepository.save(ctg.getId().getVoucher());
                    }
                }

                // Cập nhật số lượng tồn sau khi giảm giá
                chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                chiTietGiayRepository.save(chiTietGiay);
            } else {
                return; // Xử lý khi số lượng không đủ
            }

            hoaDonChiTiet.setChiTietGiay(cartItem.getId().getChiTietGiay());
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getId().getChiTietGiay().getGia());
            hoaDonChiTiet.setPhanTramGiam(phanTramGiam);
            hoaDonChiTiet.setSoLuongGiam(tongSoLuongGiam);
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            this.hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        savedHoaDon.setTongTien(total - tongTienGiam);
        savedHoaDon.setKhuyenMai(tongTienGiam);
        hoaDonWebRepository.save(savedHoaDon);
        session.setAttribute("maHD", savedHoaDon.getMaHoaDOn());


        HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
        hinhThuc.setTrangThai(hinhThucThanhToan);
        hinhThuc.setHoaDon(savedHoaDon);
        hinhThuc.setSoTien(0.0);
        hinhThucThanhToanRepository.save(hinhThuc);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setPhuongThuc("3");
        this.lichSuHoaDonWebRepository.save(lichSuHoaDon);

        for (GioHangChiTiet cartItem : cartItems) {
            gioHangChiTietRepository.delete(cartItem);
        }
        this.emailService.guiEmailXacNhanThanhToan(email, savedHoaDon);
    }


    public void capNhatDiaChi(DiaChiLoGin diaChiDTO, TaiKhoan taiKhoan) {
        Optional<DiaChi> defaultDiaChi = taiKhoan.getDiaChiList().stream()
                .filter(diaChi -> diaChi.getDiaChiMacDinh() == 1)
                .findFirst();

        defaultDiaChi.ifPresent(existingDefaultDiaChi -> {
            diaChiDTO.loadDiaChiDTO(existingDefaultDiaChi); // Cập nhật thông tin địa chỉ
            this.diaChiTamChu.save(existingDefaultDiaChi);
        });
    }

    public List<GioHangChiTiet> getCartItemsByEmail(String email) {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
        if (taiKhoan != null) {
            GioHang gioHang = this.gioHangService.getCartByEmail(email);
            if (gioHang != null) {
                return gioHangService.getCartItems(gioHang.getId());
            }
        }
        return new ArrayList<>();
    }

}

