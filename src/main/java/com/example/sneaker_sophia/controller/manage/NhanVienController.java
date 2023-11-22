package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.dto.NhanVienDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.repository.VaiTroRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.example.sneaker_sophia.service.DiaChiService;
import com.example.sneaker_sophia.service.FileUpload;
import com.example.sneaker_sophia.service.TaiKhoanService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/nhanvien")
@RequiredArgsConstructor
public class NhanVienController {

    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;

    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @GetMapping("/hienthi")
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

    @GetMapping("/create")
    public String create(Model model, HttpSession session) {
        session.setAttribute("tinh", "-1");
        session.setAttribute("quan", "-1");
        session.setAttribute("phuong", "-1");
        TaiKhoanRequest nhanVienRequest = new TaiKhoanRequest();
        model.addAttribute("nhanVienRequest", nhanVienRequest);
        nhanVienRequest.setGioiTinh(1);
        return "admin/nhanvien/createnv";
    }

    @GetMapping("/edit/{id}")
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
        session.setAttribute("anhDaiDien", taiKhoanDiaChi.getAnhDaiDien());

        return "admin/nhanvien/editnv";
    }
    private final FileUpload fileUpload;

    @PostMapping("/store")
    public String create(
            Model model,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute(value = "nhanVienRequest") TaiKhoanRequest nv_rq, HttpSession session
    ) throws IOException {

        nv_rq.setIdVaiTro(vaiTroRepository.getIdByTenNV());
        String imageURL = "";
        if(multipartFile.isEmpty()){
            imageURL = "thumbnail.png";
        }else{
            imageURL = fileUpload.uploadFile(multipartFile);
        }
        nv_rq.setAnhDaiDien(imageURL);
        if (taiKhoanService.save(nv_rq, model)) {
            if (nv_rq.getTinh() == null){
                session.setAttribute("tinh", "-1");
                session.setAttribute("quan", "-1");
                session.setAttribute("phuong", "-1");
            }else{
                session.setAttribute("tinh", nv_rq.getTinh());
                session.setAttribute("quan", nv_rq.getQuanHuyen());
                session.setAttribute("phuong", nv_rq.getPhuongXa());
                session.setAttribute("anhDaiDien", nv_rq.getAnhDaiDien());
            }

            return "admin/nhanvien/createnv";
        }
        return "redirect:/admin/nhanvien/hienthi";

    }

    @PostMapping("/update/{id}")
    public String update(
            Model model,
            @PathVariable("id") String idTaiKhoan,
            @RequestParam("image") MultipartFile multipartFile,
            @ModelAttribute("nhanVien") TaiKhoanRequest nv_rq
    ) throws IOException {
        TaiKhoanRequest taiKhoan = taiKhoanService.getTaiKhoanById(idTaiKhoan);
        nv_rq.setIdTaiKhoan(idTaiKhoan);
        String imageURL = null;
        if(multipartFile.isEmpty()){
            nv_rq.setAnhDaiDien(taiKhoan.getAnhDaiDien());
        }else {
            imageURL = fileUpload.uploadFile(multipartFile);
        }
        nv_rq.setAnhDaiDien(imageURL);
        nv_rq.setIdVaiTro(vaiTroRepository.getIdByTenNV());
        taiKhoanService.update(idTaiKhoan, nv_rq, model);
        return "redirect:/admin/nhanvien/hienthi";
    }
}