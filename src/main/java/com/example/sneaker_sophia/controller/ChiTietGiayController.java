package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class ChiTietGiayController {
    @Autowired
    ChiTietGiayService chiTietGiayService;
    @Autowired
    GiayService giayService;
    @Autowired
    HangService hangService;
    @Autowired
    DeGiayService deGiayService;
    @Autowired
    MauSacService mauSacService;
    @Autowired
    LoaiGiayService loaiGiayService;
    @Autowired
    KichCoService kichCoService;
//    @Autowired
//    AnhService anhService;
//    @Autowired
//    HttpServletRequest request;

    @GetMapping("chi-tiet-giay")
    public String listFirstPage(Model model){
//        HttpSession session = request.getSession();
//        if(session.getAttribute("admin") == null ){
//            return "redirect:/login-admin" ;
//        }
        return listByPage(1,model,"gia","asc",null,null,null,null,null,null,null,null,null,null,null);
    }
    @GetMapping("chi-tiet-giay/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                              @RequestParam(name = "sortField", required = false, defaultValue = "defaultSortField") String sortField,
                              @RequestParam(name = "sortDir", required = false, defaultValue = "defaultSortDir") String sortDir,
                              @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
                              @RequestParam(name = "productName", required = false, defaultValue = "") String productName,
                              @RequestParam(name = "giay", required = false, defaultValue = "defaultGiay") String giay,
                              @RequestParam(name = "deGiay", required = false, defaultValue = "defaultDeGiay") String deGiay,
                              @RequestParam(name = "hang", required = false, defaultValue = "defaultHang") String hang,
                              @RequestParam(name = "loaiGiay", required = false, defaultValue = "defaultLoaiGiay") String loaiGiay,
                              @RequestParam(name = "mauSac", required = false, defaultValue = "defaultMauSac") String mauSac,
                              @RequestParam(name = "kichCo", required = false, defaultValue = "defaultKichCo") String kichCo,
                              @Param("giaMin") Double giaMin,
                              @Param("giaMax") Double giaMax,
                              @RequestParam Map<String, String> params) {
        Page<ChiTietGiay> page;

        if ((keyword != null && !keyword.isEmpty()) || (productName != null && !productName.isEmpty())) {
            page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName);
        } else if ((giay != null && !giay.equals("defaultGiay")) || (deGiay != null && !deGiay.equals("defaultDeGiay")) || (hang != null && !hang.equals("defaultHang")) || (loaiGiay != null && !loaiGiay.equals("defaultLoaiGiay")) || (mauSac != null && !mauSac.equals("defaultMauSac")) || (kichCo != null && !kichCo.equals("defaultKichCo")) || (giaMin != null && giaMax != null)) {
            page = chiTietGiayService.filterCombobox(pageNum, sortField, sortDir,
                    giayService.findByTen(giay),
                    deGiayService.findByTen(deGiay),
                    hangService.findByTen(hang),
                    loaiGiayService.findByTen(loaiGiay),
                    mauSacService.findByTen(mauSac),
                    kichCoService.findByTen(kichCo),
                    giaMin,
                    giaMax
            );
        } else {
            page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName);
        }


        List<ChiTietGiay> listChiTietSanPham = page.getContent();


        int startCount = (pageNum - 1) * chiTietGiayService.PRODUCT_DETAIL_PER_PAGE + 1;
        long endCount = startCount + chiTietGiayService.PRODUCT_DETAIL_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItem", page.getTotalElements());
        model.addAttribute("listChiTietGiay", listChiTietSanPham);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("productName", productName);
        model.addAttribute("deGiayList", deGiayService.getAll());
        model.addAttribute("loaiGiayList", loaiGiayService.getAll());
        model.addAttribute("mauSacList", mauSacService.getAll());
        model.addAttribute("kichCoList", kichCoService.getAll());
        model.addAttribute("giayList", giayService.getAll());
        model.addAttribute("hangList", hangService.getAll());

// để giữ cac giá trị combobõx
        model.addAttribute("loaiGiay", params != null ? params.get("loaiGiay") : null);
        model.addAttribute("deGiay", params != null ? params.get("deGiay") : null);
        model.addAttribute("kichCo", params != null ? params.get("kichCo") : null);
        model.addAttribute("mauSac", params != null ? params.get("mauSac") : null);
        model.addAttribute("hang", params != null ? params.get("hang") : null);
        model.addAttribute("giay", params != null ? params.get("giay") : null);
        model.addAttribute("giaMin", params != null ? params.get("giaMin") : null);
        model.addAttribute("giaMax", params != null ? params.get("giaMax") : null);

        return "admin/chiTietGiay/chiTietGiay";
    }
}
