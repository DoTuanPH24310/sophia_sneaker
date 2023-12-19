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
    private GioHangRepository gioHangRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;

    public void thucHienThanhToan(String email, DiaChiLoGin diaChiLoGin, List<GioHangChiTiet> cartItems, Integer hinhThucThanhToan, String diaChi,
                                  String tinh, String huyen, String xa, Double phiVanChuyen, String ghiChu) {
        TaiKhoan taiKhoan = this.loginRepository.findByEmail(email);
        taiKhoan.setTrangThai(1);
        GioHang gioHang = this.gioHangRepository.findByTaiKhoan(taiKhoan);
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
        if (hinhThucThanhToan == 3) {
            hoaDon.setTrangThai(3);
        } else if (hinhThucThanhToan == 2) {
            hoaDon.setTrangThai(2);
        }
        hoaDon.setTenKhachHang(taiKhoan.getTen());
        hoaDon.setSoDienThoai(taiKhoan.getSdt());
        hoaDon.setDiaChi(diaChi + ", " + xa + ", " + huyen + ", " + tinh);
        hoaDon.setPhiShip(phiVanChuyen);
        hoaDon.setTongTien(0.0);
        hoaDon.setTienThua(0.0);
        hoaDon.setTongTien(tongTienDonHang);
        hoaDon.setKhuyenMai(tongTienGiam);
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setCreatedBy(taiKhoan.getTen());
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

                        tongSoLuongGiam = soLuongGiamApDung;
                        phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                        int giamGia = phanTramGiam * soLuongGiam;
                        tongGiamGia = giamGia;
                        double donGia = chiTietGiay.getGia();
                        int giam = ctg.getId().getVoucher().getPhanTramGiam();
                        double tienGiam = donGia * giam / 100 * soLuongGiamApDung;
                        tongTienGiam += tienGiam;
                        tongTienDonHang = total - tongTienGiam;
                        soLuongPhieuGiamDaSuDung += soLuongGiamApDung;

                        ctg.getId().getVoucher().setSoLuong(ctg.getId().getVoucher().getSoLuong() - soLuongGiamApDung);
                        voucherRepository.save(ctg.getId().getVoucher());
                    }
                }
                if(hoaDon.getTrangThai() != 2) {
                    chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                }
                chiTietGiayRepository.save(chiTietGiay);
            } else {
                return;
            }

            hoaDonChiTiet.setChiTietGiay(cartItem.getId().getChiTietGiay());
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getId().getChiTietGiay().getGia());
            hoaDonChiTiet.setPhanTramGiam(phanTramGiam);
            hoaDonChiTiet.setSoLuongGiam(tongSoLuongGiam);
            hoaDonChiTiet.setHoaDon(savedHoaDon);
            this.hoaDonChiTietRepository.save(hoaDonChiTiet);
            savedHoaDon.getListHoaDonChiTiet().add(hoaDonChiTiet);

        }
        savedHoaDon.setTongTien(total - tongTienGiam);
        savedHoaDon.setKhuyenMai(tongTienGiam);
        savedHoaDon = capNhatDiaChi(diaChiLoGin, taiKhoan, diaChi, tinh, huyen, xa, savedHoaDon);

        hoaDonWebRepository.save(savedHoaDon);
        session.setAttribute("maHD", savedHoaDon.getMaHoaDOn());

        HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
        hinhThuc.setTrangThai(hinhThucThanhToan);
        hinhThuc.setHoaDon(savedHoaDon);
        hinhThuc.setSoTien(0.0);
        hinhThucThanhToanRepository.save(hinhThuc);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        if (hinhThucThanhToan == 3) {
            lichSuHoaDon.setPhuongThuc("3");
        } else if (hinhThucThanhToan == 2) {
            lichSuHoaDon.setPhuongThuc("2");
        }
        this.lichSuHoaDonWebRepository.save(lichSuHoaDon);

        for (GioHangChiTiet cartItem : cartItems) {
            gioHangChiTietRepository.delete(cartItem);
        }
        session.setAttribute("mailTaiKhoan", email);
        if (hinhThucThanhToan == 3) {
            this.emailService.guiEmailXacNhanThanhToan(email, savedHoaDon);
        }
        gioHang.setTrangThai(0);
        this.gioHangRepository.save(gioHang);
    }


    public HoaDon capNhatDiaChi(DiaChiLoGin diaChiDTO, TaiKhoan taiKhoan, String diaChic,
                                String tinh, String huyen, String xa, HoaDon hoaDon) {
        Optional<DiaChi> defaultDiaChi = taiKhoan.getDiaChiList().stream()
                .filter(diaChiItem -> diaChiItem.getDiaChiMacDinh() == 1)
                .findFirst();
        if (!defaultDiaChi.isPresent()) {
            DiaChi newDiaChi = new DiaChi();
            diaChiDTO.loadDiaChiDTO(newDiaChi);
            newDiaChi.setTaiKhoan(taiKhoan);
            diaChiTamChu.save(newDiaChi);
            hoaDon.setDiaChi(diaChic + ", " + xa + ", " + huyen + ", " + tinh);
        } else {
            // Cập nhật thông tin địa chỉ mặc định nếu có
            defaultDiaChi.ifPresent(existingDefaultDiaChi -> {
                diaChiDTO.loadDiaChiDTO(existingDefaultDiaChi);
                diaChiTamChu.save(existingDefaultDiaChi);
                hoaDon.setDiaChi(diaChic + ", " + xa + ", " + huyen + ", " + tinh);
            });
        }

        return hoaDonWebRepository.save(hoaDon);
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

