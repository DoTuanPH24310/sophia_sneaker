package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/tai-quay")
public class TaiQuayController {
    // trạnh thái = 2 (chờ)tai-quay
    @Resource(name = "hoaDonService")
    HoaDonService hoaDonService;

    @Resource(name = "hoaDonChiTietServive")
    HoaDonChiTietServive hoaDonChiTietServive;

    @GetMapping("/hien-thi")
    public String index(Model model) {
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "/admin/taiquay/index";
    }


    @GetMapping("/open-sanpham")
    public String showModal(Model model) {
        model.addAttribute("modalSanPham", true);
        return "/admin/taiquay/index";

    }

    @RequestMapping("/addHD")
    public String addHD(
    ) {
        hoaDonService.addHD();
        return "redirect:/admin/tai-quay/hien-thi";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable("id") String id,
            Model model
    ){
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(id);
        model.addAttribute("listhdct", listhdct);
        return "/admin/taiquay/index";
    }

// =================================================================
//    @GetMapping("/pdf")
//    public void pdf(HttpServletResponse response) throws IOException {
//        SinhVien sv = new SinhVien("Sv01", "Hoang", 10);
//        SinhVien sv1 = new SinhVien("Sv02", "Cường", 10000);
//        SinhVien sv2 = new SinhVien("Sv03", "nam", 200000000);
//        List<SinhVien> list = new ArrayList<>(List.of(sv, sv1, sv2));
//
//        response.setContentType("application/pdf");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users.pdf";
//        response.setHeader(headerKey, headerValue);
//
//        export(response,list);
//
//    }
//
//    public void export(HttpServletResponse response,List<SinhVien> list) throws IOException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//
//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setSize(54);
//        font.setColor(Color.BLUE);
//        Paragraph p = new Paragraph("List of User", font);
//
//        document.add(p);
//
//        PdfPTable table = new PdfPTable(3);
//        table.setWidthPercentage(100f);
//        table.setWidths(new float[]{1.5f, 3.5f, 3.0f});
//        table.setSpacingBefore(10);
//
//        writeTableHeader(table);
//        writeTableData(table,list);
//        document.add(table);
//        document.close();
//    }
//
//    private void writeTableData(PdfPTable table, List<SinhVien> list) {
//        for (SinhVien user : list) {
//            table.addCell(user.getMa());
//            table.addCell(user.getTên());
//            table.addCell(String.valueOf(user.getTuổi()));
//        }
//    }
//
//    public void writeTableHeader(PdfPTable table) {
//        PdfPCell cell = new PdfPCell();
//
//        cell.setBackgroundColor(Color.BLUE);
//        cell.setPadding(5);
//
//
//        cell.setPhrase(new Phrase("Mã Sinh viên"));
//        table.addCell(cell);
//
//        cell.setPhrase(new Phrase("Tên sinh viên"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Tuổi"));
//        table.addCell(cell);
//
//
//    }
//
//
//    @Getter
//    @Setter
//    @AllArgsConstructor
//    @NoArgsConstructor
//    class SinhVien {
//        String ma;
//
//        String tên;
//
//        Integer tuổi;
//
//
//    }


}
