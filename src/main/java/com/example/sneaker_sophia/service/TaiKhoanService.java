package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.NhanVienDTO;
import com.example.sneaker_sophia.dto.TaiKhoanDiaChi;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.entity.VaiTro;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import com.example.sneaker_sophia.repository.TaiKhoanRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service("taiKhoanService")
public class TaiKhoanService {

    @Resource(name = "taiKhoanRepository")
    TaiKhoanRepository taiKhoanRepository;

    @Resource(name = "diaChiRepository")
    DiaChiRepository diaChiRepository;

    public Page<NhanVienDTO> getAllNhanVien(String search, Integer trangThai, Pageable pageable) {
        Page<TaiKhoan> taiKhoanPage = taiKhoanRepository.getALlNhanVienTT(search, trangThai, pageable);
        List<NhanVienDTO> nhanVienDTOS = taiKhoanPage.getContent().stream().map(NhanVienDTO::new).collect(Collectors.toList());
        Page<NhanVienDTO> nhanVienPage = new PageImpl<>(nhanVienDTOS, pageable, taiKhoanPage.getTotalElements());
        return nhanVienPage;
    }

    public Page<NhanVienDTO> getAllKhachHang(String search, Integer trangThai, Pageable pageable) {
        Page<TaiKhoan> taiKhoanPage = taiKhoanRepository.getALlKhachHang(search, trangThai, pageable);
        List<NhanVienDTO> khachHangDTOS = taiKhoanPage.getContent().stream().map(NhanVienDTO::new).collect(Collectors.toList());
        Page<NhanVienDTO> khachHangPage = new PageImpl<>(khachHangDTOS, pageable, taiKhoanPage.getTotalElements());
        return khachHangPage;
    }

    public TaiKhoanRequest getTaiKhoanById(String id) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        DiaChi diaChi = diaChiRepository.getDiaChiByIdTaiKhoan(id);
        TaiKhoanDiaChi taiKhoanDiaChi = new TaiKhoanDiaChi(taiKhoan, diaChi);
        TaiKhoanRequest nhanVienRequest = new TaiKhoanRequest(taiKhoanDiaChi);
        return nhanVienRequest;
    }


    public boolean save(TaiKhoanRequest nhanVienRequest, Model model) {
        int check = 0;
        TaiKhoan taiKhoan = new TaiKhoan(nhanVienRequest);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        taiKhoan.setMatKhau(encodedPassword);
        taiKhoan.setAnhDaiDien(nhanVienRequest.getAnhDaiDien());
        taiKhoan.setTrangThai(1);
        DiaChi diaChi = new DiaChi(nhanVienRequest);
        diaChi.setTaiKhoan(taiKhoan);
        diaChi.setDiaChiMacDinh(1);
        diaChi.setTen(nhanVienRequest.getTen());
        diaChi.setSdt(nhanVienRequest.getSdt());

        if (nhanVienRequest.getAnhDaiDien().equals("")) {
            taiKhoan.setAnhDaiDien("thumbnail.png");
        }
        this.taiKhoanRepository.save(taiKhoan);
        this.diaChiRepository.save(diaChi);


        return true;
    }

    public boolean update(String id, TaiKhoanRequest nhanVienRequest, Model model) {
        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        DiaChi diaChi = diaChiRepository.getDiaChiByIdTaiKhoan(id);
        if (taiKhoan != null && diaChi != null) {
            taiKhoan.setTen(nhanVienRequest.getTen());
            taiKhoan.setEmail(nhanVienRequest.getEmail());
            taiKhoan.setNgaySinh(LocalDate.parse(nhanVienRequest.getNgaySinh()));
            taiKhoan.setGioiTinh(Integer.parseInt(nhanVienRequest.getGioiTinh()));
            taiKhoan.setCanCuoc(nhanVienRequest.getCanCuoc());
            taiKhoan.setSdt(nhanVienRequest.getSdt());
            taiKhoan.setTrangThai(nhanVienRequest.getTrangThai());
            taiKhoan.setAnhDaiDien(nhanVienRequest.getAnhDaiDien());
            taiKhoan.setVaiTro(VaiTro.builder().id(nhanVienRequest.getIdVaiTro()).build());

            diaChi.setDiaChiCuThe(nhanVienRequest.getDiaChiCuThe());
            diaChi.setTinh(nhanVienRequest.getTinh());
            diaChi.setQuanHuyen(nhanVienRequest.getQuanHuyen());
            diaChi.setPhuongXa(nhanVienRequest.getPhuongXa());
            diaChi.setTaiKhoan(TaiKhoan.builder().id(nhanVienRequest.getIdTaiKhoan()).build());
            diaChi.setDiaChiMacDinh(1);
            diaChi.setTrangThai(1);
            if (nhanVienRequest.getAnhDaiDien() == null) {
                taiKhoan.setAnhDaiDien(taiKhoanRepository.getAnhById(id));
            }
            this.taiKhoanRepository.save(taiKhoan);
            this.diaChiRepository.save(diaChi);
        }
        return true;
    }

    public boolean validateAdd(TaiKhoanRequest nhanVienRequest, Model model) {
        int i = 0;
        String errTen = null, errEmail = null, errCCCD = null, errSDT = null, errGT = null, errTrangThai = null, errTinh = null, errQuanHuyen = null, errPhuongXa = null, errDCCuThe = null, errNgaySinh = null;
        LocalDate gioHT = LocalDate.now();
        String regex = "^(0\\d{9}|(\\+|00)84\\d{9})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nhanVienRequest.getSdt());

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(nhanVienRequest.getEmail());

        String idCardRegex = "^\\d{12}$";
        Pattern idCardPattern = Pattern.compile(idCardRegex);
        Matcher idCardMatcher = idCardPattern.matcher(nhanVienRequest.getCanCuoc());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate ngaySinh = LocalDate.parse(String.valueOf(nhanVienRequest.getNgaySinh()), formatter);
            if (nhanVienRequest.getNgaySinh() == null) {
                errNgaySinh = "Không để trống ngày sinh";
                i++;
            } else if (ngaySinh.isAfter((gioHT))) {
                errNgaySinh = "Ngày sinh không hợp lệ";
                i++;
            }
        } catch (Exception e) {
            errNgaySinh = "Nhập đúng định dạng ngày";
            i++;
        }
        if (!matcher.matches()) {
            errSDT = "Số điện thoại không đúng định dạng (10 số)";
            i++;
        }
        if (!emailMatcher.matches()) {
            errEmail = "Email không đúng định dạng";
            i++;
        }
        if (!idCardMatcher.matches()) {
            errCCCD = "Số CCCD không đúng định dạng (12 số)";
            i++;
        }
        if (taiKhoanRepository.getTaiKhoanByEmail(nhanVienRequest.getEmail()) != null) {
            errEmail = "Email đã được sử dụng";
            i++;
        }
        if (taiKhoanRepository.getTaiKhoanByCCCD(nhanVienRequest.getCanCuoc()) != null) {
            errCCCD = "CCCD đã được sử dụng";
            i++;
        }
        if (taiKhoanRepository.getTaiKhoanBySDT(nhanVienRequest.getSdt()) != null) {
            errSDT = "Số điện thoại đã được sử dụng";
            i++;
        }
        if (nhanVienRequest.getTen().equals("")) {
            errTen = "Không để trống tên";
            i++;
        }
        if (nhanVienRequest.getEmail().equals("")) {
            errEmail = "Không để trống email";
            i++;
        }
        if (nhanVienRequest.getCanCuoc().equals("")) {
            errCCCD = "Không để trống tên";
            i++;
        }
        if (nhanVienRequest.getSdt().equals("")) {
            errSDT = "Không để trống số điện thoại";
            i++;
        }

        if (nhanVienRequest.getGioiTinh() == null) {
            errGT = "Không để trống giới tính";
            i++;
        }
        if (!nhanVienRequest.getGioiTinh().equals("1") && !nhanVienRequest.getGioiTinh().equals("0")) {
            errGT = "Thao tác không chính xác";
            i++;
        }

        if (nhanVienRequest.getTinh() == null) {
            errTinh = "Không để trống Tỉnh/Thành phố";
            i++;
        }
        if (nhanVienRequest.getQuanHuyen() == null) {
            errQuanHuyen = "Không để trống Quận/Huyện";
            i++;
        }
        if (nhanVienRequest.getPhuongXa() == null) {
            errPhuongXa = "Không để trống Phường/Xã";
            i++;
        }
        if (nhanVienRequest.getDiaChiCuThe().equals("")) {
            errDCCuThe = "Không để trống địa chỉ cụ thể";
            i++;
        }
        model.addAttribute("errNgaySinh", errNgaySinh);
        model.addAttribute("errTen", errTen);
        model.addAttribute("errEmail", errEmail);
        model.addAttribute("errCCCD", errCCCD);
        model.addAttribute("errNgaySinh", errNgaySinh);
        model.addAttribute("errSDT", errSDT);
        model.addAttribute("errGT", errGT);
        model.addAttribute("errTrangThai", errTrangThai);
        model.addAttribute("errTinh", errTinh);
        model.addAttribute("errQuanHuyen", errQuanHuyen);
        model.addAttribute("errPhuongXa", errPhuongXa);
        model.addAttribute("errDCCuThe", errDCCuThe);
        return i == 0;
    }

    public boolean validateUppdate(TaiKhoanRequest nhanVienRequest, Model model) {
        int i = 0;
        String errTen = null, errEmail = null, errCCCD = null, errSDT = null, errGT = null, errTrangThai = null, errTinh = null, errQuanHuyen = null, errPhuongXa = null, errDCCuThe = null, errNgaySinh = null;
        LocalDate gioHT = LocalDate.now();
        String regex = "^(0\\d{9}|(\\+|00)84\\d{9})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nhanVienRequest.getSdt());

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(nhanVienRequest.getEmail());

        String idCardRegex = "^\\d{12}$";
        Pattern idCardPattern = Pattern.compile(idCardRegex);
        Matcher idCardMatcher = idCardPattern.matcher(nhanVienRequest.getCanCuoc());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            LocalDate ngaySinh = LocalDate.parse(String.valueOf(nhanVienRequest.getNgaySinh()), formatter);
            if (nhanVienRequest.getNgaySinh() == null) {
                errNgaySinh = "Không để trống ngày sinh";
                i++;
            } else if (ngaySinh.isAfter((gioHT))) {
                errNgaySinh = "Ngày sinh không hợp lệ";
                i++;
            }
        } catch (Exception e) {
            errNgaySinh = "Nhập đúng định dạng ngày";
            i++;
        }
        if (!matcher.matches()) {
            errSDT = "Số điện thoại không đùng định dạng (10 số)";
            i++;
        }
        if (!emailMatcher.matches()) {
            errEmail = "Email không đúng định dạng";
            i++;
        }
        if (!idCardMatcher.matches()) {
            errCCCD = "Số CCCD không đúng định dạng (12 số)";
            i++;
        }
        if (nhanVienRequest.getTen().equals("")) {
            errTen = "Không để trống tên";
            i++;
        }
        if (nhanVienRequest.getEmail().equals("")) {
            errEmail = "Không để trống email";
            i++;
        }
        if (nhanVienRequest.getCanCuoc().equals("")) {
            errCCCD = "Không để trống tên";
            i++;
        }
        if (nhanVienRequest.getSdt().equals("")) {
            errSDT = "Không để trống số điện thoại";
            i++;
        }
        if (nhanVienRequest.getGioiTinh() == null) {
            errGT = "Không để trống giới tính";
            i++;
        }
        if (nhanVienRequest.getTrangThai() == null) {
            errTrangThai = "Không để trống trạng thái";
            i++;
        }
        if (nhanVienRequest.getTinh() == null) {
            errTinh = "Không để trống Tỉnh/Thành phố";
            i++;
        }
        if (nhanVienRequest.getQuanHuyen() == null) {
            errQuanHuyen = "Không để trống Quận/Huyện";
            i++;
        }
        if (nhanVienRequest.getPhuongXa() == null) {
            errPhuongXa = "Không để trống Phường/Xã";
            i++;
        }
        if (nhanVienRequest.getDiaChiCuThe().equals("")) {
            errDCCuThe = "Không để trống địa chỉ cụ thể";
            i++;
        }

        model.addAttribute("errNgaySinh", errNgaySinh);
        model.addAttribute("errTen", errTen);
        model.addAttribute("errEmail", errEmail);
        model.addAttribute("errCCCD", errCCCD);
        model.addAttribute("errNgaySinh", errNgaySinh);
        model.addAttribute("errSDT", errSDT);
        model.addAttribute("errGT", errGT);
        model.addAttribute("errTrangThai", errTrangThai);
        model.addAttribute("errTinh", errTinh);
        model.addAttribute("errQuanHuyen", errQuanHuyen);
        model.addAttribute("errPhuongXa", errPhuongXa);
        model.addAttribute("errDCCuThe", errDCCuThe);
        return i == 0;
    }

    public TaiKhoan getTaiKhoanByIdKH(String idkh) {
        return taiKhoanRepository.findById(idkh).orElse(null);
    }

    //30/10
    public List<TaiKhoan> findAllKhachHang() {
        return taiKhoanRepository.findAllKhachHang();
    }

    public List<TaiKhoan> findByText(String text) {
        text = text.trim().length() == 0 ? null : "%" + text + "%";
        return taiKhoanRepository.findByText(text);
    }

    public List<DiaChi> findListTKById(String id) {
        return diaChiRepository.findListTKByIdKH((id));
    }

}