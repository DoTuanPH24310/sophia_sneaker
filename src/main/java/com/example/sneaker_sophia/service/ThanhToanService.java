package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.HinhThucThanhToanWebRepository;
import com.example.sneaker_sophia.repository.HoaDonChiTietWebRepository;
import com.example.sneaker_sophia.repository.HoaDonWebRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ThanhToanService {
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private HoaDonWebRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietWebRepository hoaDonChiTietRepository;

    @Autowired
    private HinhThucThanhToanWebRepository hinhThucThanhToanRepository;

    public void thucHienThanhToan(String email, List<GioHangChiTiet> cartItems, Integer hinhThucThanhToan) {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
        int i = 1;
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDOn("HD"+ i++);
        hoaDon.setTenKhachHang(taiKhoan.getTen());
        hoaDon.setSoDienThoai(taiKhoan.getSdt());
        hoaDon.setTaiKhoan(taiKhoan);

        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        for (GioHangChiTiet cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            hoaDonChiTiet.setHoaDon(savedHoaDon); // Gán hóa đơn cho hóa đơn chi tiết
            hoaDonChiTiet.setChiTietGiay(cartItem.getId().getChiTietGiay());
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getId().getChiTietGiay().getGia());
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
        hinhThuc.setPhuongThuc(hinhThucThanhToan);
        hinhThuc.setHoaDon(savedHoaDon);
        hinhThucThanhToanRepository.save(hinhThuc);


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

