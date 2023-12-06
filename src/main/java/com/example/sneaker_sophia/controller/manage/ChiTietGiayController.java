package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
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

    private final FileUpload fileUpload;



    @GetMapping("/staff/chi-tiet-giay")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "ngayTao", "asc", null, null, null, null, null, null, null, null, "0", null, null, null);
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
                              @RequestParam(name = "trangThai", required = false,defaultValue = "-1") String trangThai,
                              @Param("giaMin") Double giaMin,
                              @Param("giaMax") Double giaMax,
                              @RequestParam Map<String, String> params) {
        Page<ChiTietGiay> page;

        if ((keyword != null && !keyword.isEmpty()) || (productName != null && !productName.isEmpty())) {
            page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName,trangThai);
        } else if ((giay != null && !giay.equals("defaultGiay"))
                || (deGiay != null && !deGiay.equals("defaultDeGiay"))
                || (hang != null && !hang.equals("defaultHang"))
                || (loaiGiay != null && !loaiGiay.equals("defaultLoaiGiay"))
                || (mauSac != null && !mauSac.equals("defaultMauSac"))
                || (kichCo != null && !kichCo.equals("defaultKichCo"))
                || (trangThai != null && !trangThai.equals("-1"))
                || (giaMin != null && giaMax != null)) {
            page = chiTietGiayService.filterCombobox(pageNum, sortField, sortDir,
                    giayService.findByTen(giay),
                    deGiayService.findByTen(deGiay),
                    hangService.findByTen(hang),
                    loaiGiayService.findByTen(loaiGiay),
                    mauSacService.findByTen(mauSac),
                    kichCoService.findByTen(kichCo),
                    trangThai,
                    giaMin,
                    giaMax
            );
        } else {
            page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName,trangThai);
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
        model.addAttribute("hangList", hangService.getAll());
        model.addAttribute("trangThai", trangThai);

// để giữ cac giá trị combobõx
        model.addAttribute("loaiGiay", params != null ? params.get("loaiGiay") : null);
        model.addAttribute("deGiay", params != null ? params.get("deGiay") : null);
        model.addAttribute("kichCo", params != null ? params.get("kichCo") : null);
        model.addAttribute("mauSac", params != null ? params.get("mauSac") : null);
        model.addAttribute("hang", params != null ? params.get("hang") : null);
        model.addAttribute("giay", params != null ? params.get("giay") : null);
        model.addAttribute("giaMin", params != null ? params.get("giaMin") : null);
        model.addAttribute("giaMax", params != null ? params.get("giaMax") : null);
        model.addAttribute("trangThai", trangThai != null ? trangThai : "-1");
        return "admin/chiTietGiay/chiTietGiay";
    }

    @GetMapping("/admin/chi-tiet-giay/add")
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


    @GetMapping("/admin/chi-tiet-giay/edit/{id}")
    public String edit(Model model, @PathVariable("id") UUID id) {
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

    @PostMapping("/admin/chi-tiet-giay/add")
    public String add(
            @ModelAttribute ChiTietGiay chiTietGiay,
            @RequestParam("imageFile") MultipartFile[] imageFiles,
            Model model) {
        try {
            // Kiểm tra hợp lệ trước khi lưu thông tin
            if (!chiTietGiayService.validate(chiTietGiay, model)) {
                model.addAttribute("giay", giayService.getAll());
                model.addAttribute("hang", hangService.getAll());
                model.addAttribute("deGiay", deGiayService.getAll());
                model.addAttribute("mauSac", mauSacService.getAll());
                model.addAttribute("loaiGiay", loaiGiayService.getAll());
                model.addAttribute("kichCo", kichCoService.getAll());
                model.addAttribute("anh", anhService.getAll());
                return "admin/chiTietGiay/formChiTietGiay";
            }
            // Lưu chi tiết giày vào cơ sở dữ liệu
            chiTietGiayService.save(chiTietGiay);

            boolean isFirstImage = true;

            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();

                // Tải ảnh lên Cloudinary và nhận URL của ảnh
                String imageUrl = fileUpload.uploadFile(imageFile);

                if (imageUrl != null) {
                    // Tạo đối tượng ảnh và thiết lập các thông tin cần thiết
                    Anh anh = new Anh();

                    if (isFirstImage) {
                        anh.setAnhChinh(1);
                        isFirstImage = false;
                    } else {
                        anh.setAnhChinh(0);
                    }

                    anh.setDuongDan(imageUrl); // Lưu URL của ảnh từ Cloudinary
                    anh.setChiTietGiay(chiTietGiay);

                    // Lưu đối tượng ảnh vào cơ sở dữ liệu
                    anhService.save(anh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/chi-tiet-giay";
    }
    @PostMapping("/admin/chi-tiet-giay/update")
    public String update(ChiTietGiay chiTietGiay,
                         @RequestParam("imageFile") MultipartFile[] imageFiles,
                         Model model) {
        try {
            // Kiểm tra hợp lệ trước khi lưu thông tin
            if (!chiTietGiayService.validateUpdate(chiTietGiay, model)) {
                model.addAttribute("giay", giayService.getAll());
                model.addAttribute("hang", hangService.getAll());
                model.addAttribute("deGiay", deGiayService.getAll());
                model.addAttribute("mauSac", mauSacService.getAll());
                model.addAttribute("loaiGiay", loaiGiayService.getAll());
                model.addAttribute("kichCo", kichCoService.getAll());
                model.addAttribute("anh", anhService.getAll());
                return "admin/chiTietGiay/formEditChiTietGiay";
            }

            // Lưu chi tiết giày vào cơ sở dữ liệu
            boolean isFirstImage = true;

            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();

                // Tải ảnh lên Cloudinary và nhận URL của ảnh
                String imageUrl = fileUpload.uploadFile(imageFile);

                if (imageUrl != null) {
                    // Tạo đối tượng ảnh và thiết lập các thông tin cần thiết
                    Anh anh = new Anh();

                    if (isFirstImage) {
                        anh.setAnhChinh(1);
                        isFirstImage = false;
                    } else {
                        anh.setAnhChinh(0);
                    }

                    anh.setDuongDan(imageUrl); // Lưu URL của ảnh từ Cloudinary
                    anh.setChiTietGiay(chiTietGiay);

                    // Lưu đối tượng ảnh vào cơ sở dữ liệu
                    anhService.save(anh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi, có thể thêm thông báo lỗi vào model để hiển thị trên giao diện
        }
        return "redirect:/admin/chi-tiet-giay";
    }

    @GetMapping("/admin/chi-tiet-giay/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        chiTietGiayService.delete(id);
        return "redirect:/admin/chi-tiet-giay";
    }

    @GetMapping("/admin/chi-tiet-giay/delete-anh/{id}")
    public String deleteAnhByChiTietGiay(@PathVariable("id") UUID id) {
        anhService.deleteAnhByChiTietGiay(chiTietGiayService.getOne(id));
        return "redirect:/admin/chi-tiet-giay/edit/{id}";
    }

    @GetMapping("/admin/anh/delete-anh/{id}")
    public String deleteAnhById(@PathVariable("id") String id) {
        UUID uuid = chiTietGiayService.findChiTietGiayIdByAnhId(id);
        anhService.delete(id);
        return "redirect:/admin/chi-tiet-giay/edit/"+uuid;
    }
}
