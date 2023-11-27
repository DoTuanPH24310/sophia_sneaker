package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public void thucHienThanhToan(String email, List<GioHangChiTiet> cartItems, Integer hinhThucThanhToan) {
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
        hoaDon.setDiaChi(diaChiTamChu.taoDiaChiString(taiKhoan.getDiaChiList()));
        hoaDon.setPhiShip(20000.0);
        hoaDon.setTrangThai(3);
        hoaDon.setTongTien(0.0);
        hoaDon.setTienThua(0.0);
        hoaDon.setKhuyenMai(tongTienGiam);
        HoaDon savedHoaDon = hoaDonWebRepository.save(hoaDon);
        for (GioHangChiTiet cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();

            int soLuongMua = cartItem.getSoLuong();
            int soLuongHienTai = chiTietGiay.getSoLuong();

            if (soLuongHienTai >= soLuongMua) {
                List<CTG_KhuyenMai> listCTG_KM = chiTietGiay.getListCTG_KM();
                int soLuongPhieuGiamDaSuDung = 0;

                for (CTG_KhuyenMai ctg : listCTG_KM) {
                    if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                        int soLuongGiamApDung = Math.min(ctg.getId().getVoucher().getSoLuong() - soLuongPhieuGiamDaSuDung, soLuongMua);

                        double donGia = chiTietGiay.getGia();
                        phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                        double tienGiam = donGia * phanTramGiam / 100 * soLuongGiamApDung;

                        tongSoLuongGiam += soLuongGiamApDung;
                        tongGiamGia += phanTramGiam;
                        tongTienGiam += tienGiam;
                        soLuongPhieuGiamDaSuDung += soLuongGiamApDung;

                        ctg.getId().getVoucher().setSoLuong(ctg.getId().getVoucher().getSoLuong() - soLuongGiamApDung);
                        voucherRepository.save(ctg.getId().getVoucher());
                    }
                }

                chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                chiTietGiayRepository.save(chiTietGiay);
            } else {
                return; // Xử lý khi số lượng không đủ
            }

            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setChiTietGiay(cartItem.getId().getChiTietGiay());
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getId().getChiTietGiay().getGia());
            hoaDonChiTiet.setPhanTramGiam(tongGiamGia);
            hoaDonChiTiet.setSoLuongGiam(tongSoLuongGiam);
            this.hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        savedHoaDon.setTongTien(total - tongTienGiam);
        savedHoaDon.setKhuyenMai(tongTienGiam);
        hoaDonWebRepository.save(savedHoaDon);


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

