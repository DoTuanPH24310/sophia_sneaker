package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class EmailService {

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;
    @Autowired
    private LoginRepository taiKhoanRepository;

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
    private PasswordEncoder passwordEncoder;

    public TaiKhoan taoTaiKhoanMoi(DiaChiDTO diaChiDTO) {
        TaiKhoan taiKhoanMoi = new TaiKhoan();

        if (diaChiDTO != null && diaChiDTO.getTaiKhoan() != null) {
            taiKhoanMoi.setTen(diaChiDTO.getTen());
            taiKhoanMoi.setEmail(diaChiDTO.getTaiKhoan().getEmail());
            taiKhoanMoi.setTrangThai(1);
            String matKhauNgauNhien = taoMatKhauNgauNhien();
            String hashedMatKhau = passwordEncoder.encode(matKhauNgauNhien);
            taiKhoanMoi.setMatKhau(hashedMatKhau);
            System.out.println("matkhau1"+matKhauNgauNhien);

            VaiTro vaiTro = this.vaiTroRepository.findById("7A76E301-D2CF-42EA-AC77-76C5E2487513").orElse(null);
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
        for (CartItem cartItem : cartItems) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();

            hoaDonChiTiet.setSoLuong(cartItem.getSoLuong());
            hoaDonChiTiet.setDonGia(cartItem.getGia());
            hoaDonChiTiet.setTrangThai(1);
            ChiTietGiay chiTietGiay = chiTietGiayRepository.findById(cartItem.getId()).orElse(null);
            System.out.println("Chi tiet giay: " + chiTietGiay.getId());

            if (chiTietGiay != null) {
                hoaDonChiTiet.setChiTietGiay(chiTietGiay);
                hoaDonChiTiet.setHoaDon(hoaDon);

                hoaDon.getListHoaDonChiTiet().add(hoaDonChiTiet);
                System.out.println("Hoa don chi tiet: " + hoaDon.getListHoaDonChiTiet());
                this.hoaDonChiTietWebRepository.save(hoaDonChiTiet);

            } else {
                System.err.println("Product details not found for ID: " + cartItem.getId());
            }
        }

    }


    public HoaDon taoHoaDonMoi(TaiKhoan taiKhoan, Integer hinhThucThanhToan) {
        Integer i = 0;
        HoaDon hoaDonMoi = new HoaDon();
        hoaDonMoi.setMaHoaDOn("HD" + i++);
        hoaDonMoi.setTaiKhoan(taiKhoan);
        hoaDonMoi.setLoaiHoaDon(1);
        hoaDonMoi.setTenKhachHang(taiKhoan.getTen());
        hoaDonMoi.setSoDienThoai(taiKhoan.getSdt());
        hoaDonMoi.setDiaChi(diaChiTamChu.taoDiaChiString(taiKhoan.getDiaChiList()));
        hoaDonMoi.setPhiShip(20000.0);
        hoaDonMoi.setTrangThai(1);

        hoaDonMoi = this.hoaDonWebRepository.save(hoaDonMoi);

        return hoaDonMoi;
    }
}

