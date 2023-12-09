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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
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
    @Autowired
    private AlertInfo alertInfo;
    @Autowired
    ChiTietGiayDTO chiTietGiayDTO;

    private final FileUpload fileUpload;



    @GetMapping("/staff/chi-tiet-giay")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "ngayTao", "asc", null, null, null, null, null, null, null, "0", null, null, null);
    }

    @GetMapping("chi-tiet-giay/page/{pageNum}")
    private String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
                              @RequestParam(name = "sortField", required = false, defaultValue = "defaultSortField") String sortField,
                              @RequestParam(name = "sortDir", required = false, defaultValue = "defaultSortDir") String sortDir,
                              @Param("keyword") String keyword,
                              @RequestParam(name = "giay", required = false, defaultValue = "defaultGiay") String giay,
                              @RequestParam(name = "deGiay", required = false, defaultValue = "defaultDeGiay") String deGiay,
                              @RequestParam(name = "hang", required = false, defaultValue = "defaultHang") String hang,
                              @RequestParam(name = "loaiGiay", required = false, defaultValue = "defaultLoaiGiay") String loaiGiay,
                              @RequestParam(name = "mauSac", required = false, defaultValue = "defaultMauSac") String mauSac,
                              @RequestParam(name = "kichCo", required = false, defaultValue = "defaultKichCo") String kichCo,
                              @RequestParam(name = "trangThai", required = false,defaultValue = "0") String trangThai,
                              @Param("giaMin") Double giaMin,
                              @Param("giaMax") Double giaMax,
                              @RequestParam Map<String, String> params) {
        Page<ChiTietGiay> page;
            page = chiTietGiayService.filterCombobox(pageNum,
                    sortField,
                    sortDir,
                    giayService.findByTen(giay),
                    deGiayService.findByTen(deGiay),
                    hangService.findByTen(hang),
                    loaiGiayService.findByTen(loaiGiay),
                    mauSacService.findByTen(mauSac),
                    kichCoService.findByTen(kichCo),
                    trangThai,
                    giaMin,
                    giaMax,
                    keyword
            );

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
        model.addAttribute("keyword", params != null ? params.get("keyword") : null);
        model.addAttribute("trangThai", trangThai != null ? trangThai : "0");
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
            @ModelAttribute("chiTietGiay") @Validated ChiTietGiayDTO chiTietGiay,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile[] imageFiles,
            Model model) {
        try {
            // Kiểm tra lỗi validation
            if (bindingResult.hasErrors() || !chiTietGiayService.validate(chiTietGiay,imageFiles, model)) {
                model.addAttribute("giay", giayService.getAll());
                model.addAttribute("hang", hangService.getAll());
                model.addAttribute("deGiay", deGiayService.getAll());
                model.addAttribute("mauSac", mauSacService.getAll());
                model.addAttribute("loaiGiay", loaiGiayService.getAll());
                model.addAttribute("kichCo", kichCoService.getAll());
                model.addAttribute("anh", anhService.getAll());
                alertInfo.alert("errTaiQuay", "Thất bại, vui lòng kiểm tra lại");
                return "admin/chiTietGiay/formChiTietGiay";
            }
            if(chiTietGiay.getQrCode().isEmpty() || chiTietGiay.getQrCode()==null){
                chiTietGiay.setQrCode(chiTietGiayService.generateRandomQRCode());
            }
            ChiTietGiay chiTietGiaySaved = chiTietGiayService.save(chiTietGiayDTO.loadChiTietGiayDTO(chiTietGiay));
            boolean isFirstImage = true;

            for (MultipartFile imageFile : imageFiles) {
                String originalFilename = imageFile.getOriginalFilename();

                // Tải ảnh lên Cloudinary và nhận URL của ảnh
                String imageUrl = fileUpload.uploadFile(imageFile);
                System.out.println(imageUrl +"á");
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

                    anh.setChiTietGiay(chiTietGiaySaved);
                    System.out.println("assas á" + chiTietGiayDTO.loadChiTietGiayDTO(chiTietGiay));
                    // Lưu đối tượng ảnh vào cơ sở dữ liệu
                    anhService.save(anh);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        alertInfo.alert("successTaiQuay", "Thêm thành công");
        return "redirect:/admin/chi-tiet-giay";
    }
    @PostMapping("/adminchi-tiet-giay/edit/{id}")
    public String update(@PathVariable("id") String id,@ModelAttribute("chiTietGiay") @Validated ChiTietGiayDTO chiTietGiay1,
                         BindingResult bindingResult,
                         @RequestParam("imageFile") MultipartFile[] imageFiles,
                         Model model) {
        ChiTietGiay chiTietGiay = chiTietGiayService.getOne(UUID.fromString(id));
        chiTietGiay.setId(chiTietGiay1.getId());
        try {
            // Kiểm tra lỗi validation
            if (bindingResult.hasErrors() || !chiTietGiayService.validateUpdate(chiTietGiay, model)) {
                model.addAttribute("giay", giayService.getAll());
                model.addAttribute("hang", hangService.getAll());
                model.addAttribute("deGiay", deGiayService.getAll());
                model.addAttribute("mauSac", mauSacService.getAll());
                model.addAttribute("loaiGiay", loaiGiayService.getAll());
                model.addAttribute("kichCo", kichCoService.getAll());
                model.addAttribute("anh", anhService.getAll());
                model.addAttribute("anh", anhService.anhsFindIdChitietGiay(chiTietGiay));
                alertInfo.alert("errTaiQuay", "Thất bại, vui lòng kiểm tra lại");
                return "admin/chiTietGiay/formEditChiTietGiay";
            }

            System.out.println("có "+ Arrays.stream(imageFiles).count());
            // Nếu có sự thay đổi, xóa các ảnh cũ và thực hiện cập nhật
            if (Arrays.stream(imageFiles).count() > 1) {
                if (bindingResult.hasErrors() || !chiTietGiayService.validateUpdateAnh(imageFiles, model)) {
                    model.addAttribute("giay", giayService.getAll());
                    model.addAttribute("hang", hangService.getAll());
                    model.addAttribute("deGiay", deGiayService.getAll());
                    model.addAttribute("mauSac", mauSacService.getAll());
                    model.addAttribute("loaiGiay", loaiGiayService.getAll());
                    model.addAttribute("kichCo", kichCoService.getAll());
                    model.addAttribute("anh", anhService.getAll());
                    model.addAttribute("anh", anhService.anhsFindIdChitietGiay(chiTietGiay));
                    alertInfo.alert("errTaiQuay", "Thất bại, vui lòng kiểm tra lại");
                    return "admin/chiTietGiay/formEditChiTietGiay";
                }

                // Xóa các ảnh cũ
                anhService.deleteAnhByChiTietGiay(chiTietGiayService.getOne(chiTietGiay.getId()));

                // Lưu chi tiết giày vào cơ sở dữ liệu
                chiTietGiayService.save(chiTietGiay);

                // Lưu ảnh mới
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
            }else{
                // Không có sự thay đổi, giữ nguyên ảnh
                List<Anh> currentImages = anhService.anhsFindIdChitietGiay(chiTietGiay);
                for (Anh currentImage : currentImages) {
                    anhService.save(currentImage);
                }
                chiTietGiayService.save(chiTietGiay);
                chiTietGiayService.save(chiTietGiayDTO.loadChiTietGiayDTO(chiTietGiay1));
                // Đặt danh sách ảnh vào model sau khi lưu
                model.addAttribute("anh", anhService.anhsFindIdChitietGiay(chiTietGiay));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Xử lý lỗi, có thể thêm thông báo lỗi vào model để hiển thị trên giao diện
        }
        chiTietGiayService.save(chiTietGiay);
        chiTietGiayService.save(chiTietGiayDTO.loadChiTietGiayDTO(chiTietGiay1));
        alertInfo.alert("successTaiQuay", "Sửa thành công");
        return "redirect:/admin/chi-tiet-giay";
    }

    @GetMapping("/admin/chi-tiet-giay/delete/{id}")
    public String delete(@PathVariable("id") UUID id) {
        chiTietGiayService.delete(id);
        alertInfo.alert("successTaiQuay", "Xóa thành công");
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
