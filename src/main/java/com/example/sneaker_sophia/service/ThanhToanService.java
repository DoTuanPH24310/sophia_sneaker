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
    private ChiTietGiayRepository chiTietGiayRepository;
    @Autowired
    private GioHangService gioHangService;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private HoaDonWebRepository hoaDonRepository;

    @Autowired
    private DiaChiTamChu diaChiTamChu;

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


    // Trong HoaDonService
//    public void themSanPhamVaoHoaDonChiTiet(List<CartItem> cartItems, HoaDon hoaDon) {
//        // Định nghĩa logic để thêm sản phẩm vào hóa đơn chi tiết
//        for (CartItem cartItem : cartItems) {
//            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
//            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
//            hoaDonChiTiet.setDonGia(cartItem.getGia());
//            // Các thiết lập khác...
//            // Gán chi tiết giày cho sản phẩm trong hóa đơn chi tiết
//            hoaDonChiTiet.setChiTietGiay(this.chiTietGiayRepository.findById(cartItem.getId()).orElse(null)); // Thay thế bằng phương thức thích hợp
//            // Gán hóa đơn cho sản phẩm trong hóa đơn chi tiết
//            hoaDonChiTiet.setHoaDon(hoaDon);
//            // Thêm vào danh sách hóa đơn chi tiết của hóa đơn
//            hoaDon.getListHoaDonChiTiet().add(hoaDonChiTiet);
//        }
//    }
//
//    public HoaDon taoHoaDonMoi(TaiKhoan taiKhoan, Integer hinhThucThanhToan) {
//        HoaDon hoaDonMoi = new HoaDon();
//        hoaDonMoi.setTaiKhoan(taiKhoan);
//        hoaDonMoi.setLoaiHoaDon(1);  // Thay thế bằng giá trị thích hợp
//        hoaDonMoi.setTenKhachHang(taiKhoan.getTen());
//        hoaDonMoi.setSoDienThoai(taiKhoan.getSdt());
//        hoaDonMoi.setDiaChi(this.diaChiTamChu.taoDiaChiString(taiKhoan.getDiaChiList()));
//        hoaDonMoi.setPhiShip(20000.0);  // Thay thế bằng giá trị thích hợp
//        hoaDonMoi.setTrangThai(1);  // Thay thế bằng giá trị thích hợp
//        // Các thiết lập khác...
//
//        // Lưu hóa đơn vào cơ sở dữ liệu (tùy thuộc vào logic của bạn)
//        hoaDonMoi = this.hoaDonRepository.save(hoaDonMoi);
//
//        return hoaDonMoi;
//    }


}

