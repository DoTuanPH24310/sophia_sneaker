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

import java.util.List;
import java.util.Random;

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

            if (chiTietGiay != null) {
                int soLuongMua = cartItem.getSoLuong();
                int soLuongHienTai = chiTietGiay.getSoLuong();

                if (soLuongHienTai >= soLuongMua) {
                    chiTietGiay.setSoLuong(soLuongHienTai - soLuongMua);
                    chiTietGiayRepository.save(chiTietGiay);

                    hoaDonChiTiet.setChiTietGiay(chiTietGiay);
                    hoaDonChiTiet.setHoaDon(hoaDon);

                    hoaDon.getListHoaDonChiTiet().add(hoaDonChiTiet);
                    this.hoaDonChiTietWebRepository.save(hoaDonChiTiet);
                } else {
                    System.err.println("Not enough stock for product with ID: " + cartItem.getId());
                }
            } else {
                System.err.println("Product details not found for ID: " + cartItem.getId());
            }
        }

    }


    public HoaDon taoHoaDonMoi(TaiKhoan taiKhoan, Integer hinhThucThanhToan) {
        Cart cart = (Cart) session.getAttribute("cart");
        List<CartItem> cartItems = cart.getItems();
        HoaDon hoaDonMoi = new HoaDon();
        hoaDonMoi.setMaHoaDOn("HD" + this.hoaDonRepository.soHD());
        hoaDonMoi.setTaiKhoan(taiKhoan);
        hoaDonMoi.setLoaiHoaDon(3);
        hoaDonMoi.setTenKhachHang(taiKhoan.getTen());
        hoaDonMoi.setSoDienThoai(taiKhoan.getSdt());
        hoaDonMoi.setDiaChi(diaChiTamChu.taoDiaChiString(taiKhoan.getDiaChiList()));
        hoaDonMoi.setPhiShip(20000.0);
        hoaDonMoi.setTongTien(0.0);
        hoaDonMoi.setTienThua(0.0);
        hoaDonMoi.setTrangThai(3);
//        hoaDonMoi.setKhuyenMai();

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

