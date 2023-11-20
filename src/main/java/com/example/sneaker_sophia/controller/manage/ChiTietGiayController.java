package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
    public String listFirstPage(Model model) {
        return listByPage(1, model, "gia", "asc", null, null, null, null, null, null, null, null, null, null, null, null);
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
                              @RequestParam(name = "trangThai", required = false, defaultValue = "1") String trangThai,
                              @Param("giaMin") Double giaMin,
                              @Param("giaMax") Double giaMax,
                              @RequestParam Map<String, String> params) {
        Page<ChiTietGiay> page;

        if ((keyword != null && !keyword.isEmpty()) || (productName != null && !productName.isEmpty())) {
            page = chiTietGiayService.listByPageAndProductName(pageNum, sortField, sortDir, keyword, productName);
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
        model.addAttribute("trangThai", params != null ? params.get("trangThai") : null);
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
                String projectDir = System.getProperty("user.dir"); // Lấy thư mục gốc của dự án
                String uploadDir = projectDir + "/src/main/resources/static/img";

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
                String projectDir = System.getProperty("user.dir"); // Lấy thư mục gốc của dự án
                String uploadDir = projectDir + "/src/main/resources/static/img";

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

    @GetMapping("chi-tiet-giay/delete-anh/{id}")
    public String deleteAnhByChiTietGiay(@PathVariable("id") UUID id) {
        anhService.deleteAnhByChiTietGiay(chiTietGiayService.getOne(id));
        return "redirect:/admin/chi-tiet-giay/edit/{id}";
    }


    // import excel
    @GetMapping("/exportToExcel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        // Tạo một workbook mới
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Sheet1");

        // Viết dữ liệu vào sheet (Ví dụ)
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("Hello");
        // Thêm các dòng và cột khác nếu cần thiết

        // Ghi workbook vào ByteArrayOutputStream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // Chuẩn bị dữ liệu để trả về
        byte[] excelBytes = outputStream.toByteArray();

        // Tạo định dạng ngày giờ cho tên file
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = dateFormat.format(new Date());

        // Tạo tên file với ngày giờ phút giây
        String fileName = "exported_chiTietGiay_" + formattedDate + ".xlsx";

        // Thiết lập HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", fileName);

        // Trả về ResponseEntity
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

}
