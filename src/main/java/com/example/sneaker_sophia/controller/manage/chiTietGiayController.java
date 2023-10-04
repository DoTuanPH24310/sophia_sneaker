package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.service.ChiTietGiayService;
import com.example.sneaker_sophia.service.iplm.ChiTietGiayIplm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/")
public class chiTietGiayController {

    @Autowired
    ChiTietGiayService chiTietGiayService;

    @Autowired
    HttpServletRequest request;

    @GetMapping("chi-tiet-giay")
    public String index(Model model) {
        List<ChiTietGiay> listChiTietGiay = chiTietGiayService.getAll();
        model.addAttribute("listChiTietGiay", listChiTietGiay);
        return "admin/chiTietGiay/chiTietGiay";
    }

    @GetMapping("chi-tiet-giay/add")
    public String add(Model model) {

        return "admin/chiTietGiay/formChiTietGiay";
    }
//    @GetMapping("chi-tiet-giay/page/{pageNum}")
//    private String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
//                              @Param("sortField") String sortField, @Param("sortDir") String sortDir,
//                              @Param("keyword") String keyword,
//                              @Param("productName") String productName) {
//        HttpSession session = request.getSession();
//        if (session.getAttribute("admin") == null) {
//            return "redirect:/login-admin";
//        }
//
//        Page<ChiTietGiay> page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName);
//        List<ChiTietGiay> listChiTietSanPham = page.getContent();
//
//        long startCount = (pageNum - 1) * chiTietGiayService.PRODUCT_DETAIL_PER_PAGE + 1;
//        long endCount = startCount + chiTietGiayService.PRODUCT_DETAIL_PER_PAGE - 1;
//        if (endCount > page.getTotalElements()) {
//            endCount = page.getTotalElements();
//        }
//        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("startCount", startCount);
//        model.addAttribute("endCount", endCount);
//        model.addAttribute("totalItem", page.getTotalElements());
//        model.addAttribute("listChiTietSanPham", listChiTietSanPham);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("reverseSortDir", reverseSortDir);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("productName", productName);
//
//        return "admin/chiTietGiay/chiTietGiay";
//    }

}
