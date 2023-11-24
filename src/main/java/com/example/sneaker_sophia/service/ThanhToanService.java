package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
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
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;

    public void thucHienThanhToan(String email, List<GioHangChiTiet> cartItems, Integer hinhThucThanhToan) {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
        double total = 0.0;
        for (GioHangChiTiet cartItem : cartItems) {
            total += cartItem.getId().getChiTietGiay().getGia() * cartItem.getSoLuong();
        }
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDOn("HD" + this.hoaDonRepository.soHD());
        hoaDon.setTaiKhoan(taiKhoan);
        hoaDon.setLoaiHoaDon(3);
        hoaDon.setTenKhachHang(taiKhoan.getTen());
        hoaDon.setSoDienThoai(taiKhoan.getSdt());
        hoaDon.setDiaChi(diaChiTamChu.taoDiaChiString(taiKhoan.getDiaChiList()));
        hoaDon.setPhiShip(20000.0);
        hoaDon.setTrangThai(3);
        hoaDon.setTongTien(total);
        hoaDon.setTienThua(0.0);
        HoaDon savedHoaDon = hoaDonWebRepository.save(hoaDon);
        for (GioHangChiTiet cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietGiay chiTietGiay = cartItem.getId().getChiTietGiay();

            int soLuongMua = cartItem.getSoLuong();
            int soLuongHienTai = chiTietGiay.getSoLuong();

            if (soLuongHienTai >= soLuongMua) {
                chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                chiTietGiayRepository.save(chiTietGiay);
            }else{
                return;
            }
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            hoaDonChiTiet.setChiTietGiay(cartItem.getId().getChiTietGiay());
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getId().getChiTietGiay().getGia());
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
        hinhThuc.setTrangThai(hinhThucThanhToan);
        hinhThuc.setHoaDon(savedHoaDon);
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

