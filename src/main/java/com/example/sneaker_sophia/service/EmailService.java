package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmailService {

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;
    @Autowired
    HttpSession session;
    @Autowired
    private LoginRepository taiKhoanRepository;

    @Autowired
    private HinhThucThanhToanWebRepository hinhThucThanhToanWebRepository;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private DiaChiTamChu diaChiTamChu;

    @Autowired
    private HoaDonWebRepository hoaDonWebRepository;

    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    @Autowired
    private HoaDonChiTietWebRepository hoaDonChiTietWebRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private LichSuHoaDonWebRepository lichSuHoaDonWebRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "hoaDonRepository")
    HoaDonRepository hoaDonRepository;


    public TaiKhoan taoTaiKhoanMoi(DiaChiDTO diaChiDTO) {
        TaiKhoan taiKhoanMoi = new TaiKhoan();

        if (diaChiDTO != null) {
            taiKhoanMoi.setTen(diaChiDTO.getTen());
            taiKhoanMoi.setEmail(diaChiDTO.getEmail());
            taiKhoanMoi.setTrangThai(1);
            String matKhauNgauNhien = taoMatKhauNgauNhien();
            String hashedMatKhau = passwordEncoder.encode(matKhauNgauNhien);
            taiKhoanMoi.setMatKhau(hashedMatKhau);
            System.out.println("matkhau1" + matKhauNgauNhien);

            VaiTro vaiTro = this.vaiTroRepository.findByTen("ADMIN");
            taiKhoanMoi.setVaiTro(vaiTro);
            taiKhoanMoi.setSdt(diaChiDTO.getSdt());
            taiKhoanMoi = this.taiKhoanRepository.save(taiKhoanMoi);
//            guiEmailDangKyTaiKhoan(taiKhoanMoi.getEmail(), matKhauNgauNhien);
        } else {
            System.err.println("DiaChiDTO or TaiKhoan is null.");
        }

        return taiKhoanMoi;
    }


    public void themDiaChiVaoTaiKhoan(DiaChiDTO diaChiDTO, TaiKhoan taiKhoan) {
        DiaChi diaChiMoi = new DiaChi();
        diaChiDTO.loadDiaChiDTO(diaChiMoi);
        taiKhoan.setEmail(diaChiDTO.getEmail());
        diaChiMoi.setTrangThai(1);
        diaChiMoi.setDiaChiMacDinh(1);


        if (taiKhoan.getId() == null) {
            this.taiKhoanRepository.save(taiKhoan);
        }

        this.diaChiTamChu.save(diaChiMoi);

        taiKhoan.getDiaChiList().add(diaChiMoi);
        this.taiKhoanRepository.save(taiKhoan);
    }

    public void guiEmailDangKyTaiKhoan(String email, String matKhau) {
        if (email == null) {
            System.err.println("Email address is null");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Cảm ơn bạn đã sử mua hàng tại sophia-store");
        message.setText("Chúng tôi đã nhận được yêu cầu của bạn." +
                "\n để theo dõi đơn hàng bạn vui lòng đăng nhập email " +
                "\n Với mật khẩu của bạn là: " + matKhau);

        guiEmail(message);
    }

    public void guiEmailOTP(String email, String matKhau) {
        if (email == null) {
            System.err.println("Email address is null");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Cảm ơn bạn đã sử mua hàng tại sophia-store");
        message.setText("Chúng tôi đã nhận được yêu cầu của bạn." +
                "\n  Mã OTP của bạn là: " + matKhau);

        guiEmail(message);
    }

    public void guiEmailXacNhanThanhToan(String email, HoaDon hoaDon) {
        if (email == null) {
            System.err.println("Email address is null");
            return;
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Xác nhận đơn hàng");
        message.setText("Cảm ơn bạn đã thanh toán đơn hàng. Dưới đây là chi tiết đơn hàng:\n" +
                "Mã đơn hàng: " + hoaDon.getMaHoaDOn() + "\n" +
                "Người nhận: " + hoaDon.getTaiKhoan().getTen() + "\n" +
                "Địa chỉ nhận hàng: " + hoaDon.getDiaChi() + "\n" +
                "Tổng tiền đơn hàng: " + hoaDon.getTongTien() + "\n" +
                "Trạng thái: " + hoaDon.getTrangThai());

        guiEmail(message);
    }

    private void guiEmail(SimpleMailMessage message) {
        try {
            javaMailSender.send(message);
            System.out.println("Email đã được gửi thành công.");
        } catch (MailException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    // Các phương thức khác

    public String taoMatKhauNgauNhien() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder matKhau = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            matKhau.append(characters.charAt(random.nextInt(characters.length())));
        }
        return matKhau.toString();
    }

    public void themSanPhamVaoHoaDonChiTiet(List<CartItem> cartItems, HoaDon hoaDon) {
        double total = 0.0;
        double tongTienDonHang = 0.0;
        int tongSoLuongGiam = 0;
        Integer tongGiamGia = 0;
        int phanTramGiam = 0;
        int soLuongGiam = 0;
        double tongTienGiam = 0.0;
        Map<UUID, Integer> soLuongGiamTheoSanPham = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(cartItem.getId()).orElse(null);
            total += chiTietGiay.getGia() * cartItem.getSoLuong();

            int soLuongMua = cartItem.getSoLuong();
            int soLuongHienTai = chiTietGiay.getSoLuong();
            int soLuongPhieuGiamDaSuDung = soLuongGiamTheoSanPham.getOrDefault(cartItem.getId(), 0);
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
                        soLuongGiamTheoSanPham.put(cartItem.getId(), soLuongPhieuGiamDaSuDung + soLuongGiamApDung);
                        session.setAttribute("tongSoLuongGiam" + chiTietGiay.getId(), tongSoLuongGiam);
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

                chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                chiTietGiayRepository.save(chiTietGiay);
            } else {
                return; // Xử lý khi số lượng không đủ
            }

            chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
            chiTietGiayRepository.save(chiTietGiay);
            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(chiTietGiay.getGia());
            hoaDonChiTiet.setChiTietGiay(chiTietGiay);
            hoaDonChiTiet.setPhanTramGiam(phanTramGiam);
            hoaDonChiTiet.setSoLuongGiam(soLuongGiamTheoSanPham.getOrDefault(cartItem.getId(), 0));
            hoaDonChiTiet.setHoaDon(hoaDon);

            hoaDon.getListHoaDonChiTiet().add(hoaDonChiTiet);
            this.hoaDonChiTietWebRepository.save(hoaDonChiTiet);
        }
        hoaDon.setTongTien(tongTienDonHang);
        hoaDon.setKhuyenMai(tongTienGiam);
        this.hoaDonRepository.save(hoaDon);

    }


    public HoaDon taoHoaDonMoi(TaiKhoan taiKhoan, Integer hinhThucThanhToan, String diaChi, String tinh, String huyen, String xa) {
        HoaDon hoaDonMoi = new HoaDon();
        int soHD = this.hoaDonRepository.soHD() + 1;
        hoaDonMoi.setMaHoaDOn("HD" + soHD);
        hoaDonMoi.setTaiKhoan(taiKhoan);
        hoaDonMoi.setLoaiHoaDon(3);
        hoaDonMoi.setTenKhachHang(taiKhoan.getTen());
        hoaDonMoi.setSoDienThoai(taiKhoan.getSdt());
        hoaDonMoi.setDiaChi(diaChi + ", " + xa + ", " + huyen + ", " + tinh);
        hoaDonMoi.setPhiShip(20000.0);
        hoaDonMoi.setTienThua(0.0);
        if(hinhThucThanhToan == 3) {
            hoaDonMoi.setLoaiHoaDon(3);
        }else if(hinhThucThanhToan == 2){
            hoaDonMoi.setLoaiHoaDon(2);
        }

        hoaDonMoi = this.hoaDonWebRepository.save(hoaDonMoi);

        session.setAttribute("maHD", hoaDonMoi.getMaHoaDOn());
        HinhThucThanhToan hinhThuc = new HinhThucThanhToan();
        hinhThuc.setTrangThai(hinhThucThanhToan);
        hinhThuc.setHoaDon(hoaDonMoi);
        hinhThuc.setSoTien(0.0);
        hinhThucThanhToanWebRepository.save(hinhThuc);

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDonMoi);
        lichSuHoaDon.setPhuongThuc("3");
        this.lichSuHoaDonWebRepository.save(lichSuHoaDon);

        return hoaDonMoi;
    }
}

