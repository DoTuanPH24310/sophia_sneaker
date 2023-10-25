package com.example.sneaker_sophia.controller.manage;

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
    @Autowired
    AnhService anhService;
    @Autowired
    HttpServletRequest request;

    @GetMapping("chi-tiet-giay")
    public String listFirstPage(Model model){
//        HttpSession session = request.getSession();
//        if(session.getAttribute("admin") == null ){
//            return "redirect:/login-admin" ;
//        }
        return listByPage(1,model,"gia","asc",null,null,null,null,null,null,null,null,null);
    }
    @GetMapping("chi-tiet-giay/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                              @Param("sortField") String sortField,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword,
                              @Param("productName") String productName,
                              @Param("giay") String giay,
                              @Param("deGiay") String deGiay,
                              @Param("hang") String hang,
                              @Param("loaiGiay") String loaiGiay,
                              @Param("mauSac") String mauSac,
                              @Param("kichCo") String kichCo,
                              @RequestParam Map<String, String> params) {
        Page<ChiTietGiay> page;
        if(giay!=null||deGiay!=null||hang!=null||loaiGiay!=null||mauSac!=null||kichCo!=null){
             page = chiTietGiayService.filterCombobox(pageNum, sortField, sortDir,
                     giayService.findByTen(giay),
                     deGiayService.findByTen(deGiay),
                     hangService.findByTen(hang),
                     loaiGiayService.findByTen(loaiGiay),
                     mauSacService.findByTen(mauSac),
                     kichCoService.findByTen(kichCo)
             );
        }else{
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
        model.addAttribute("loaiGiay", params.get("loaiGiay"));
        model.addAttribute("deGiay", params.get("deGiay"));
        model.addAttribute("kichCo", params.get("kichCo"));
        model.addAttribute("mauSac", params.get("mauSac"));
        model.addAttribute("hang", params.get("hang"));
        model.addAttribute("giay", params.get("giay"));
        return "admin/chiTietGiay/chiTietGiay";
    }

    @GetMapping("chi-tiet-giay/add")
    public String formAdd(Model model) {
        ChiTietGiay chiTietGiay = new ChiTietGiay();
        model.addAttribute("chiTietGiay", chiTietGiay);
        model.addAttribute("giay", giayService.getAll());
        model.addAttribute("hang", hangService.getAll());
        model.addAttribute("deGiay", deGiayService.getAll());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("loaiGiay", loaiGiayService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("anh", anhService.getAll());
        return "admin/chiTietGiay/formChiTietGiay";
    }


    @GetMapping("chi-tiet-giay/edit/{id}")
    public String edit(Model model, @PathVariable("id")UUID id) {
        ChiTietGiay chiTietGiay = chiTietGiayService.getOne(id);
        model.addAttribute("chiTietGiay", chiTietGiay);
        model.addAttribute("giay", giayService.getAll());
        model.addAttribute("hang", hangService.getAll());
        model.addAttribute("deGiay", deGiayService.getAll());
        model.addAttribute("mauSac", mauSacService.getAll());
        model.addAttribute("loaiGiay", loaiGiayService.getAll());
        model.addAttribute("kichCo", kichCoService.getAll());
        model.addAttribute("anh", anhService.anhsFindIdChitietGiay(chiTietGiay));

        return "admin/chiTietGiay/formEditChiTietGiay";
    }

    @PostMapping("/chi-tiet-giay/save")
    public String add(
            @ModelAttribute ChiTietGiay chiTietGiay,
            @RequestParam("imageFile") MultipartFile[] imageFiles) {
        try {
            // Lưu chi tiết giày vào cơ sở dữ liệu
            chiTietGiayService.save(chiTietGiay);

            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();

                // Tạo một đối tượng ảnh và thiết lập các thông tin cần thiết
                Anh anh = new Anh();
                anh.setAnhChinh(originalFilename);
                anh.setDuongDan(originalFilename);
                anh.setChiTietGiay(chiTietGiay);

                // Lưu đối tượng ảnh vào cơ sở dữ liệu
                anhService.save(anh);

                // Lưu tệp hình ảnh vào thư mục trên máy chủ
                String uploadDir = "C:/Users/Tuan1/IdeaProjects/sophia_sneaker/src/main/resources/static/img";
                Path uploadPath = Paths.get(uploadDir);

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(originalFilename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/chi-tiet-giay";
    }


    @PostMapping("chi-tiet-giay/update")
    public String update(ChiTietGiay chiTietGiay,
                         @RequestParam("imageFile") MultipartFile[] imageFiles) {
        try {
            // Lưu chi tiết giày vào cơ sở dữ liệu
            chiTietGiayService.save(chiTietGiay);

            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();

                // Tạo một đối tượng ảnh và thiết lập các thông tin cần thiết
                Anh anh = new Anh();
                anh.setAnhChinh(originalFilename);
                anh.setDuongDan(originalFilename);
                anh.setChiTietGiay(chiTietGiay);
                // Lưu đối tượng ảnh vào cơ sở dữ liệu
                anhService.save(anh);

                // Lưu tệp hình ảnh vào thư mục trên máy chủ
                String uploadDir = "C:/Users/Tuan1/IdeaProjects/sophia_sneaker/src/main/resources/static/img";
                Path uploadPath = Paths.get(uploadDir);

                try (InputStream inputStream = imageFile.getInputStream()) {
                    Files.copy(inputStream, uploadPath.resolve(originalFilename));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        chiTietGiayService.save(chiTietGiay);
        return "redirect:/admin/chi-tiet-giay";
    }

    @GetMapping("chi-tiet-giay/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        chiTietGiayService.delete(id);
        return "redirect:/admin/chi-tiet-giay";
    }
}
