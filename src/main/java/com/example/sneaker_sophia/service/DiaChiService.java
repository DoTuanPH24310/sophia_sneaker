package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.DiaChiRepository;
import com.example.sneaker_sophia.repository.TaiKhoanRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("diaChiService")
public class DiaChiService {
    @Resource(name = "diaChiRepository")
    DiaChiRepository diaChiRepository;

    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    @Resource(name = "taiKhoanRepository")
    TaiKhoanRepository taiKhoanRepository;
    public  DiaChi getDiaChiByIdTaiKhoan(String id){
        return  diaChiRepository.getDiaChiByIdTaiKhoan(id);
    }

    public DiaChi getNhanVienDTOById(String id){
        return diaChiRepository.getNhanVienDTOById(id);
    }

    // 15-11
    public List<DiaChi> getAllDCByIdkh(String idkh){
        return diaChiRepository.findListDCByIdKH(idkh);
    }

    public void updateDCMD(String iddc, HttpSession session){
        String idkh = (String) session.getAttribute("idkh");
        DiaChi diaChiThuong = diaChiRepository.findById(iddc).orElse(null);
        DiaChi diaChiMD = diaChiRepository.getDiaChiByIdTaiKhoan(idkh);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiRepository.save(diaChiMD);
        }
        if (diaChiThuong.getDiaChiMacDinh() == 0) {
            diaChiThuong.setDiaChiMacDinh(1);
            diaChiRepository.save(diaChiThuong);
        }
    }

    public void adddc(Integer xa, Integer quan, Integer tinh, String dcCuThe, String hoTen, String sdt, HttpSession session){
        String idkh = (String) session.getAttribute("idkh");
        List<DiaChi> listDC = diaChiRepository.findListDCByIdKH(idkh);
        DiaChi diaChi = new DiaChi();
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByIdKH(idkh);
        DiaChi diaChiMD = diaChiRepository.getDiaChiByIdTaiKhoan(idkh);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiRepository.save(diaChiMD);
        }
        diaChi.setTaiKhoan(taiKhoan);
        diaChi.setPhuongXa(xa);
        diaChi.setQuanHuyen(quan);
        diaChi.setTinh(tinh);
        diaChi.setDiaChiCuThe(dcCuThe);
        diaChi.setTen(hoTen);
        diaChi.setSdt(sdt);
        diaChi.setDiaChiMacDinh(1);
        diaChiRepository.save(diaChi);
    }

    public void deleteById(String id) {
        diaChiRepository.deleteById(id);
    }


    // cuongdv


    // 31-10

    public DiaChi getDiaChiById(String iddc) {
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public void saveDC(DiaChi diaChi) {
        diaChiRepository.save(diaChi);
    }

    public List<DiaChi> findListTKById(String idkh) {
        return diaChiRepository.findListTKByIdKH(idkh);
    }

    // 3-11

    public DiaChi findDcByIdDc(String iddc) {
        return diaChiRepository.findById(iddc).orElse(null);
    }

    public DiaChi findListTKByIdKHAndDCMD(String idkh) {
        return diaChiRepository.findListTKByIdKHAndDCMD(idkh);
    }


    //    14/11
    public Integer getCountDiaChi(String idTaiKhoan) {
        return diaChiRepository.getCountDiaChi(UUID.fromString(idTaiKhoan));
    }

    public boolean validateAddDc(String dcCuThe, String hoTen, String sdt, Model model) {
        int i = 0;
        String errTen = null, errSDT = null, errDCCuThe = null, errEmail = null;

        String regex = "^(0\\d{9}|(\\+|00)84\\d{9})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sdt);

        if (!matcher.matches()) {
            errSDT = "Số điện thoại không đùng định dạng (10 số)";
            i++;
        }
        if (sdt.equals("")) {
            errSDT = "Không để trống số điện thoại";
            i++;
        }
        if (hoTen.equals("")) {
            errTen = "Không để trống tên";
            i++;
        }
        if(dcCuThe.equals("")){
            errDCCuThe = "Không để trống địa chỉ cụ thể";
            i++;
        }
        model.addAttribute("errEmail", errEmail);
        model.addAttribute("errTen", errTen);
        model.addAttribute("errSDT", errSDT);
        model.addAttribute("errDCCuThe", errDCCuThe);
        return i == 0;
    }

    private static boolean containsSpecialCharactersKH(String input) {
        // Biểu thức chính quy kiểm tra xem có kí tự đặc biệt nào trong chuỗi hay không
        String regex = "[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|.<>\\?]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

    public boolean validateAddKH(String dcCuThe, String hoTen, String email, String sdt, Integer tinh, Integer quan, Integer xa, Model model) {
        int i = 0;
        String errTen = null, errSDT = null, errTinh = null, errQuanHuyen = null, errPhuongXa = null, errDCCuThe = null, errEmail = null;

        String regex = "^(0\\d{9}|(\\+|00)84\\d{9})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sdt);
        if (!email.equals("")) {
            String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com";
            Pattern emailPattern = Pattern.compile(emailRegex);
            Matcher emailMatcher = emailPattern.matcher(email);

            if (!emailMatcher.matches()) {
                errEmail = "Email không đúng định dạng";
                i++;
            }
            if (taiKhoanRepository.getTaiKhoanByEmail(email) != null) {
                errEmail = "Email đã được sử dụng";
                i++;
            }
        }
        if (!matcher.matches()) {
            errSDT = "Số điện thoại không đùng định dạng (10 số)";
            i++;
        }
        if (sdt.equals("")) {
            errSDT = "Không để trống số điện thoại";
            i++;
        }
        if (hoTen.equals("")) {
            errTen = "Không để trống tên";
            i++;
        }
        if (tinh == null) {
            errTinh = "Không để trống Tỉnh/Thành phố";
            i++;
        }
        if (quan == null) {
            errQuanHuyen = "Không để trống Quận/Huyện";
            i++;
        }
        if (xa == null) {
            errPhuongXa = "Không để trống Phường/Xã";
            i++;
        }
        if (dcCuThe.trim().equals("")) {
            errDCCuThe = "Không để trống địa chỉ cụ thể";
            i++;
        }
        if (dcCuThe.trim().length() > 150) {
            errDCCuThe = "Địa chỉ cụ thể không vượt quá 150 kí tự";
            i++;
        }
        if (dcCuThe.trim().length() < 5) {
            errDCCuThe = "Địa chỉ cụ thể tối thiểu 5 kí tự";
            i++;
        }
        if (containsSpecialCharactersKH(dcCuThe)) {
            errDCCuThe = "Địa chỉ cụ thể không được chứa kí tự đặc biệt";
            i++;
        }
        model.addAttribute("errTinh", errTinh);
        model.addAttribute("errQuanHuyen", errQuanHuyen);
        model.addAttribute("errPhuongXa", errPhuongXa);
        model.addAttribute("errEmail", errEmail);
        model.addAttribute("errTen", errTen);
        model.addAttribute("errSDT", errSDT);
        model.addAttribute("errDCCuThe", errDCCuThe);
        return i == 0;
    }

}
