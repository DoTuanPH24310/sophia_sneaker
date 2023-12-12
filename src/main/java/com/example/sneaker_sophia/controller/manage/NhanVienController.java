package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.dto.NhanVienDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.VaiTroRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.example.sneaker_sophia.service.DiaChiService;
import com.example.sneaker_sophia.service.FileUpload;
import com.example.sneaker_sophia.service.TaiKhoanService;
import com.example.sneaker_sophia.validate.AlertInfo;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NhanVienController {

    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;

    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @Autowired
    private AlertInfo alertInfo;

    @GetMapping("/staff/nhanvien/hienthi")
    public String index(
            Model model,
            @Parameter(hidden = true) Pageable pageable,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "textSearch", required = false) String search,
            @RequestParam(value = "trangThai", required = false) Integer trangThai,
            HttpSession session
    ) {
        Page<NhanVienDTO> list = taiKhoanService.getAllNhanVien(search, trangThai, pageable);
        model.addAttribute("listNV", list);


        pageable = PageRequest.of(pageNo, 5);
        model.addAttribute("textSearch", search);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("listNV", list);
        model.addAttribute("tongNV", list.getTotalElements());
        model.addAttribute("pageNo", pageNo);
        return "admin/nhanvien/indexnv";
    }

    @GetMapping("/admin/nhanvien/create")
    public String create(Model model, HttpSession session) {
        session.setAttribute("tinh", "-1");
        session.setAttribute("quan", "-1");
        session.setAttribute("phuong", "-1");
        TaiKhoanRequest nhanVienRequest = new TaiKhoanRequest();
        model.addAttribute("nhanVienRequest", nhanVienRequest);
        nhanVienRequest.setGioiTinh(String.valueOf(1));
        return "admin/nhanvien/createnv";
    }

    @GetMapping("/admin/nhanvien/edit/{id}")
    public String editView(
            Model model,
            @PathVariable("id") String id, HttpSession session
    ) {
//        DiaChi taiKhoan = diaChiService.getNhanVienDTOById(id);
        TaiKhoanRequest taiKhoanDiaChi = taiKhoanService.getTaiKhoanById(id);
        DiaChi diaChiList = diaChiService.getDiaChiByIdTaiKhoan(id);
        model.addAttribute("nhanVien", taiKhoanDiaChi);
        session.setAttribute("tinh", diaChiList.getTinh());
        session.setAttribute("quan", diaChiList.getQuanHuyen());
        session.setAttribute("phuong", diaChiList.getPhuongXa());

        return "admin/nhanvien/editnv";
    }

    @GetMapping("/staff/nhanvien/infor/{id}")
    public String infor(
            Model model,
            @PathVariable("id") String id, HttpSession session
    ) {
//        DiaChi taiKhoan = diaChiService.getNhanVienDTOById(id);
        TaiKhoanRequest taiKhoanDiaChi = taiKhoanService.getTaiKhoanById(id);
        DiaChi diaChiList = diaChiService.getDiaChiByIdTaiKhoan(id);
        model.addAttribute("nhanVien", taiKhoanDiaChi);
        session.setAttribute("tinh", diaChiList.getTinh());
        session.setAttribute("quan", diaChiList.getQuanHuyen());
        session.setAttribute("phuong", diaChiList.getPhuongXa());
        return "admin/nhanvien/infor";
    }

    private final FileUpload fileUpload;

    @PostMapping("/admin/nhanvien/store")
    public String create(
            Model model,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute(value = "nhanVienRequest") TaiKhoanRequest nv_rq, HttpSession session
    ) throws IOException {

        nv_rq.setIdVaiTro(vaiTroRepository.getIdByTenNV());
        String imageURL = "";
        session.removeAttribute("tinh");
        session.removeAttribute("quan");
        session.removeAttribute("phuong");
        if (!taiKhoanService.validateAddNV(nv_rq, model, multipartFile)) {
            session.setAttribute("tinh", nv_rq.getTinh());
            session.setAttribute("quan", nv_rq.getQuanHuyen());
            session.setAttribute("phuong", nv_rq.getPhuongXa());
            return "admin/nhanvien/createnv";

        } else {
            if (multipartFile.isEmpty()) {
                imageURL = "thumbnail.png";
            } else {
                imageURL = fileUpload.uploadFile(multipartFile);
            }
            nv_rq.setAnhDaiDien(imageURL);
            taiKhoanService.save(nv_rq, model);
            alertInfo.alert("successTaiQuay", "Nhân viên đã được thêm");
            return "redirect:/staff/nhanvien/hienthi";

        }

    }

    @PostMapping("/admin/nhanvien/update/{id}")
    public String update(
            Model model,
            @PathVariable("id") String idTaiKhoan,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute("nhanVien") TaiKhoanRequest nv_rq, HttpSession session
    ) throws IOException {
        TaiKhoanRequest taiKhoan = taiKhoanService.getTaiKhoanById(idTaiKhoan);
        nv_rq.setIdTaiKhoan(idTaiKhoan);
        String imageURL = null;

        nv_rq.setIdVaiTro(taiKhoan.getIdVaiTro());
        if (!taiKhoanService.validateUppdate(idTaiKhoan, nv_rq, model, multipartFile)) {
            session.setAttribute("tinh", nv_rq.getTinh());
            session.setAttribute("quan", nv_rq.getQuanHuyen());
            session.setAttribute("phuong", nv_rq.getPhuongXa());
            return "admin/nhanvien/editnv";
        }
        if (multipartFile.isEmpty()) {
            nv_rq.setAnhDaiDien(taiKhoan.getAnhDaiDien());
        } else {
            imageURL = fileUpload.uploadFile(multipartFile);
        }
        nv_rq.setAnhDaiDien(imageURL);
        taiKhoanService.update(idTaiKhoan, nv_rq, model);
        alertInfo.alert("successTaiQuay", "Nhân viên đã được cập nhật");
        return "redirect:/staff/nhanvien/hienthi";


    }


}