package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DiaChiDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.text.NumberFormat;
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
        TaiKhoan taiKhoanMoi = null;

        if (diaChiDTO != null && !StringUtils.isEmpty(diaChiDTO.getEmail())) {
            taiKhoanMoi = new TaiKhoan();
            taiKhoanMoi.setTen(diaChiDTO.getTen());
            taiKhoanMoi.setEmail(diaChiDTO.getEmail());
            taiKhoanMoi.setTrangThai(1);
            taiKhoanMoi.setGioiTinh(1);
            String matKhauNgauNhien = taoMatKhauNgauNhien();
            String hashedMatKhau = passwordEncoder.encode(matKhauNgauNhien);
            taiKhoanMoi.setMatKhau(hashedMatKhau);
            System.out.println("matkhau1" + matKhauNgauNhien);

            VaiTro vaiTro = this.vaiTroRepository.findByTen("KhachHang");
            taiKhoanMoi.setVaiTro(vaiTro);
            taiKhoanMoi.setSdt(diaChiDTO.getSdt());
            taiKhoanMoi = this.taiKhoanRepository.save(taiKhoanMoi);
            guiEmailDangKyTaiKhoan(diaChiDTO.getEmail(), matKhauNgauNhien);
        } else {
            System.err.println("DiaChiDTO or TaiKhoan is null, or email is empty.");
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

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;

        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Xác nhận đơn hàng");

            String trangThai = "";
            if (hoaDon.getTrangThai() == 3) {
                trangThai = "Chờ xác nhận";
            } else if (hoaDon.getTrangThai() == 2) {
                trangThai = "Chờ thanh toán";
            }
            Locale vietnameseLocale = new Locale("vi", "VN");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(vietnameseLocale);
            StringBuilder productList = new StringBuilder();
            productList.append("<table style='width: 100%; border-collapse: collapse; border: 1px solid #ddd;'>");
            productList.append("<thead><tr><th>Ảnh</th><th>Tên sản phẩm</th><th>Giá</th><th>Số lượng</th><th>Thành tiền</th></tr></thead>");
            productList.append("<tbody>");

            for (HoaDonChiTiet chiTiet : hoaDon.getListHoaDonChiTiet()) {
                ChiTietGiay chiTietGiay = chiTiet.getChiTietGiay();
                if (chiTietGiay != null) {
                    List<Anh> anhs = chiTietGiay.getAnhs();

                    if (anhs != null && !anhs.isEmpty()) {
                        String imageSource = anhs.get(0).getDuongDan();
                        productList.append("<tr>");  // Start a new row for each product
                        productList.append("<td style='text-align: center;'><img src='").append(imageSource).append("' alt='Product Image' style='max-width: 100px;'></td>");
                    }
                    double donGia = chiTiet.getDonGia();
                    String giaSanPham = currencyFormat.format(donGia);
                    productList.append("<td style='text-align: center;'>").append(chiTietGiay.getHang().getTen() + " " + chiTietGiay.getGiay().getTen() + " " + chiTietGiay.getLoaiGiay().getTen() + " " + chiTietGiay.getMauSac().getTen() + " [" + chiTietGiay.getKichCo().getTen() + "]").append("</td>");
                    productList.append("<td style='text-align: center;'>").append(giaSanPham).append("</td>");
                    productList.append("<td style='text-align: center;'>").append(chiTiet.getSoLuong()).append("</td>");
                    double totalPrice = chiTiet.getDonGia() * chiTiet.getSoLuong();
                    String formattedTotalPrice = currencyFormat.format(totalPrice);
                    productList.append("<td style='text-align: center;'>").append(formattedTotalPrice).append("</td>");
                    productList.append("</tr>");
                } else {
                    System.out.println("ChiTietGiay is null for product: " + chiTiet.getId());
                }
            }

            productList.append("</tbody>");
            productList.append("</table>");

            String formattedTotalAmount = currencyFormat.format(hoaDon.getTongTien());
            String formattedShippingFee = currencyFormat.format(hoaDon.getPhiShip());
            String tienGiam = currencyFormat.format(hoaDon.getKhuyenMai());

            String totalAmount = "<p style='margin-top: 20px;'><strong>Tổng tiền đơn hàng:</strong> " + formattedTotalAmount + " (bao gồm phí ship: " + formattedShippingFee + ")</p>";
            String tongTienGiam = "<p style='margin-top: 20px;'><strong>Số tiền giảm giá:</strong> " + tienGiam + "</p>";

            String content = "<html><body style='font-family: Arial, sans-serif;'>" +
                    "<div style='background-color: #f4f4f4; padding: 20px;'>" +
                    "<h2 style='color: #333;'>Xác nhận đơn hàng</h2>" +
                    "<p>Cảm ơn bạn đã thanh toán đơn hàng. Dưới đây là chi tiết đơn hàng:</p>" +
                    "<p><strong>Mã đơn hàng:</strong> " + hoaDon.getMaHoaDOn() + "</p>" +
                    "<p><strong>Người nhận:</strong> " + hoaDon.getTenKhachHang() + "</p>" +
                    "<p><strong>Địa chỉ nhận hàng:</strong> " + hoaDon.getDiaChi() + "</p>" +
                    productList.toString() +
                    totalAmount +
                    tongTienGiam +
                    "<p><strong>Trạng thái:</strong> " + trangThai + "</p>" +
                    "</div>" +
                    "</body></html>";


            helper.setText(content, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        javaMailSender.send(message);
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

    public void themSanPhamVaoHoaDonChiTiet(List<CartItem> cartItems, HoaDon hoaDon, Double phiVanChuyen) {
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
                    if (ctg.getId().getVoucher().getTrangThai() == 1 && ctg.getId().getVoucher().getSoLuong() > 0) {
                        if (ctg.getId().getVoucher() != null) {
                            soLuongGiam = ctg.getId().getVoucher().getSoLuong();
                        }
                        int soLuongGiamApDung = Math.min(soLuongGiam - soLuongPhieuGiamDaSuDung, soLuongMua);

                        soLuongGiamTheoSanPham.put(cartItem.getId(), soLuongPhieuGiamDaSuDung + soLuongGiamApDung);
                        session.setAttribute("tongSoLuongGiam" + chiTietGiay.getId(), tongSoLuongGiam);
                        // Cập nhật giảm giá của sản phẩm
                        phanTramGiam = ctg.getId().getVoucher().getPhanTramGiam();
                        int giamGia = phanTramGiam * soLuongGiam;
                        tongGiamGia = giamGia;
                        double donGia = chiTietGiay.getGia();
                        int giam = ctg.getId().getVoucher().getPhanTramGiam();
                        double tienGiam = donGia * giam / 100 * soLuongGiamApDung;
                        tongTienGiam += tienGiam;
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
        hoaDon.setPhiShip(phiVanChuyen);
        hoaDon.setTongTien(total - tongTienGiam);
        hoaDon.setKhuyenMai(tongTienGiam);
        this.hoaDonRepository.save(hoaDon);

    }


    public HoaDon taoHoaDonMoi(TaiKhoan taiKhoan, Integer hinhThucThanhToan, String diaChi, String tinh, String huyen, String xa, Double phiVanChuyen, String ghiChu, String ten, String soDienThoai) {
        HoaDon hoaDonMoi = new HoaDon();
        int soHD = this.hoaDonRepository.soHD() + 1;
        hoaDonMoi.setMaHoaDOn("HD" + soHD);
        hoaDonMoi.setLoaiHoaDon(3);
        if (taiKhoan != null) {
            hoaDonMoi.setTaiKhoan(taiKhoan);
            hoaDonMoi.setTenKhachHang(taiKhoan.getTen());
            hoaDonMoi.setSoDienThoai(taiKhoan.getSdt());
        } else {
            hoaDonMoi.setTenKhachHang(ten);
            hoaDonMoi.setSoDienThoai(soDienThoai);
        }
        hoaDonMoi.setDiaChi(diaChi + ", " + xa + ", " + huyen + ", " + tinh);
        hoaDonMoi.setPhiShip(phiVanChuyen);
        hoaDonMoi.setTienThua(0.0);
        hoaDonMoi.setGhiChu(ghiChu);
        if (hinhThucThanhToan == 3) {
            hoaDonMoi.setTrangThai(3);
        } else if (hinhThucThanhToan == 2) {
            hoaDonMoi.setTrangThai(2);
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
        if (hinhThucThanhToan == 3) {
            lichSuHoaDon.setPhuongThuc("3");
        } else if (hinhThucThanhToan == 2) {
            lichSuHoaDon.setPhuongThuc("2");
        }
        this.lichSuHoaDonWebRepository.save(lichSuHoaDon);

        return hoaDonMoi;
    }

    public void guiEmailHuy(String email, String ngayHuy) {
        if (email == null) {
            System.err.println("Email address is null");
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Cảm ơn bạn đã sử mua hàng tại sophia-store");
        message.setText("Đơn hàng của bạn đã được hủy vào lúc: " + ngayHuy);

        guiEmail(message);
    }
}

