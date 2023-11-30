package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.dto.NhanVienDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.VaiTroRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.example.sneaker_sophia.service.DiaChiService;
import com.example.sneaker_sophia.service.EmailService;
import com.example.sneaker_sophia.service.FileUpload;
import com.example.sneaker_sophia.service.TaiKhoanService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/khachhang")
public class KhachHangController {
    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;

    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @Autowired
    FileUpload fileUpload;

    @GetMapping("/hienthi")
    public String index(
            Model model,
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "textSearch", required = false) String search,
            @RequestParam(value = "trangThai", required = false, defaultValue = "1") Integer trangThai,
            HttpSession session
    ) {
        Page<NhanVienDTO> list = taiKhoanService.getAllKhachHang(search, trangThai, pageable);
        model.addAttribute("listKH", list);

        pageable = PageRequest.of(pageNo, 5);
        model.addAttribute("textSearch", search);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("listKH", list);
        model.addAttribute("tongKH", list.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        return "admin/khachhang/indexkh";
    }

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        session.setAttribute("tinh", "-1");
        session.setAttribute("quan", "-1");
        session.setAttribute("phuong", "-1");
        TaiKhoanRequest khachHang = new TaiKhoanRequest();
        model.addAttribute("khachHangRequest", khachHang);
        khachHang.setGioiTinh(String.valueOf(1));
        return "admin/khachhang/createkh";
    }

    @GetMapping("/edit/{id}")
    public String editView(
            Model model,
            @PathVariable("id") String id, HttpSession session
    ) {
//        DiaChi taiKhoan = diaChiService.getNhanVienDTOById(id);
        TaiKhoanRequest taiKhoanDiaChi = taiKhoanService.getTaiKhoanById(id);
        DiaChi diaChiList = diaChiService.getDiaChiByIdTaiKhoan(id);
        model.addAttribute("khachHang", taiKhoanDiaChi);
        session.setAttribute("tinh", diaChiList.getTinh());
        session.setAttribute("quan", diaChiList.getQuanHuyen());
        session.setAttribute("phuong", diaChiList.getPhuongXa());
        session.setAttribute("anhDaiDien", taiKhoanDiaChi.getAnhDaiDien());
        session.setAttribute("idkh", id);
        return "admin/khachhang/editkh";
    }

    @PostMapping("/store")
    public String create(
            Model model,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute(value = "khachHangRequest") TaiKhoanRequest kh_rq, HttpSession session
    ) throws IOException {
        kh_rq.setIdVaiTro(vaiTroRepository.getIdByTenKH());
        String imageURL = "";
        session.removeAttribute("tinh");
        session.removeAttribute("quan");
        session.removeAttribute("phuong");
        if (!taiKhoanService.validateAddKH(kh_rq, model)) {
            session.setAttribute("tinh", kh_rq.getTinh());
            session.setAttribute("quan", kh_rq.getQuanHuyen());
            session.setAttribute("phuong", kh_rq.getPhuongXa());
            return "admin/khachhang/createkh";
        }else{
            if (multipartFile.isEmpty()) {
                imageURL = "thumbnail.png";
            } else {
                imageURL = fileUpload.uploadFile(multipartFile);
            }
            kh_rq.setAnhDaiDien(imageURL);
            taiKhoanService.save(kh_rq, model);

            return "redirect:/admin/khachhang/hienthi";
        }

    }

    @PostMapping("/update/{id}")
    public String update(
            Model model,
            @PathVariable("id") String idTaiKhoan,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute("khachHang") TaiKhoanRequest kh_rq, HttpSession session
    ) throws IOException {
        TaiKhoanRequest taiKhoan = taiKhoanService.getTaiKhoanById(idTaiKhoan);
        kh_rq.setIdTaiKhoan(idTaiKhoan);
        String imageURL = null;
        if (!taiKhoanService.validateUppdateKH(idTaiKhoan, kh_rq, model)) {
            session.setAttribute("tinh", kh_rq.getTinh());
            session.setAttribute("quan", kh_rq.getQuanHuyen());
            session.setAttribute("phuong", kh_rq.getPhuongXa());
            return "admin/khachhang/editkh";
        }
        if (multipartFile.isEmpty()) {
            kh_rq.setAnhDaiDien(taiKhoan.getAnhDaiDien());
        } else {
            imageURL = fileUpload.uploadFile(multipartFile);
        }
        kh_rq.setAnhDaiDien(imageURL);
        kh_rq.setIdVaiTro(vaiTroRepository.getIdByTenKH());
        taiKhoanService.update(idTaiKhoan, kh_rq, model);
        return "redirect:/admin/khachhang/hienthi";
    }

    @GetMapping("adddc")
    private String adddc(
            @RequestParam("xa") Integer xa,
            @RequestParam("quan") Integer quan,
            @RequestParam("tinh") Integer tinh,
            @RequestParam("dcCuThe") String dcCuThe,
            @RequestParam("hoTen") String hoTen,
            @RequestParam("sdt") String sdt,
            HttpSession session
    ) {
        diaChiService.adddc(xa, quan, tinh, dcCuThe, hoTen, sdt, session);
        return "redirect:/admin/khachhang/edit/" + session.getAttribute("idkh");
    }

    @GetMapping("updateDCMD/{id}")
    public String updateDCMD(
            @PathVariable("id") String iddc, HttpSession session
    ) {
        diaChiService.updateDCMD(iddc, session);
        return "forward:/admin/khachhang/edit/" + session.getAttribute("idkh");
    }

    @GetMapping("deleteDC/{id}")
    public String delete(
            @PathVariable("id") String iddc,
            HttpSession session
    ) {
        diaChiService.deleteById(iddc);

        return "forward:/admin/khachhang/edit/" + session.getAttribute("idkh");
    }
}