package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.dto.NhanVienDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.VaiTroRepository;
import com.example.sneaker_sophia.request.NhanVienRequest;
import com.example.sneaker_sophia.service.DiaChiService;
import com.example.sneaker_sophia.service.TaiKhoanService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/nhanvien")
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

        List<NhanVienDTO> listNv = new ArrayList<>();
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
        NhanVienRequest nhanVienRequest = new NhanVienRequest();
        model.addAttribute("nhanVienRequest", nhanVienRequest);
        return "admin/nhanvien/createnv";
    }

    @GetMapping("/edit/{id}")
    public String editView(
            Model model,
            @PathVariable("id") String id, HttpSession session
    ) {
//        DiaChi taiKhoan = diaChiService.getNhanVienDTOById(id);
        NhanVienRequest taiKhoanDiaChi = taiKhoanService.getTaiKhoanById(id);
        DiaChi diaChiList = diaChiService.getDiaChiByIdTaiKhoan(id);
        model.addAttribute("nhanVien", taiKhoanDiaChi);
        session.setAttribute("tinh", diaChiList.getTinh());
        session.setAttribute("quan", diaChiList.getQuanHuyen());
        session.setAttribute("phuong", diaChiList.getPhuongXa());
        session.setAttribute("anhDaiDien", taiKhoanDiaChi.getAnhDaiDien());

        return "admin/nhanvien/editnv";
    }

    @PostMapping("/store")
    public String create(
            Model model,
            @ModelAttribute(value = "nhanVienRequest") NhanVienRequest nv_rq, HttpSession session
    ) {
        nv_rq.setIdVaiTro(vaiTroRepository.getIdByTen());
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
            @ModelAttribute("nhanVien") NhanVienRequest nv_rq
    ) {
        nv_rq.setIdTaiKhoan(idTaiKhoan);
        nv_rq.setIdVaiTro(vaiTroRepository.getIdByTen());
        BeanUtils.copyProperties(nv_rq, new TaiKhoan());
        taiKhoanService.update(idTaiKhoan, nv_rq, model);
        return "redirect:/admin/nhanvien/hienthi";
    }
}
