package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.HoaDon;
import com.example.sneaker_sophia.entity.HoaDonChiTiet;
import com.example.sneaker_sophia.service.HoaDonChiTietServive;
import com.example.sneaker_sophia.service.HoaDonService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    ) {
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        model.addAttribute("maHD", id);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(id);
        model.addAttribute("listhdct", listhdct);
        return "/admin/taiquay/index";
    }

    @GetMapping("deletehd/{id}")
    public String deleteHD(
            @PathVariable("id") String id,
            Model model
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        hoaDonService.deleteHD(hoaDon);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "/admin/taiquay/index";
    }

    @GetMapping("/deletehdct/{idctsp}")
    public String deletehdct(
            @PathVariable("idctsp") UUID idctsp,
            Model model
    ) {
        hoaDonChiTietServive.deleteByIdCTSP(idctsp);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "/admin/taiquay/index";
    }
// =================================================================
//@GetMapping("/pdf")
//public void pdf(HttpServletResponse response) throws IOException {
//    SinhVien sv = new SinhVien("1", "Giày riu (32, Đỏ)", 10);
//    SinhVien sv1 = new SinhVien("100.000", "20.000", 80000);
//    SinhVien sv2 = new SinhVien("Sv03", "nam", 200000000);
//    List<SinhVien> list = new ArrayList<>(List.of(sv, sv1, sv2));
//
////        Để thiết lập cho trình duyệt
//    response.setContentType("application/pdf");
//    String headerKey = "Content-Disposition";
//    String headerValue = "attachment; filename=users.pdf";
//    response.setHeader(headerKey, headerValue);
//    export(response, list);
//
//}
//
//    public void export(HttpServletResponse response, List<SinhVien> list) throws IOException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//        PdfPTable table = new PdfPTable(6);
//        table.setWidthPercentage(100f);
//        table.setWidths(new float[]{1.2f, 4f, 2f,3.5f, 3.5f, 3.5f});
//        table.setSpacingBefore(15);
//        addContent(document,table,list);
//        document.close();
//    }
//
//    public void addContent(Document document, PdfPTable table, List<SinhVien> list) {
//        Paragraph p;
//        List<String> pdf = new ArrayList<>();
//        pdf.add("SOPHIA-SNEAKER");
//        pdf.add("\n\n Số điện thoại: 0123456789 \n" +
//                "Email: hoangnhph24464@fpt.edu.vn \n" +
//                "Địa chỉ: Trịnh Văn Bô - Nam Từ Liêm - Hà nội \n" +
//                "Ngân hàng: MBBank - Số tài khoản: 0001541506626 \n" +
//                "Chủ tài khoản: NGUYEN HUY HOANG \n");
//        pdf.add("\nHÓA ĐƠN BÁN HÀNG");
//        pdf.add("HD0000001");
//        pdf.add("Ngày mua:   10-10-2023");
//        pdf.add("Khách hàng:   Đỗ Văn Cường");
//        pdf.add("Địa chỉ:   Hoài Đức - Hà Nội");
//        pdf.add("Số điện thoại:   0123456789");
//        pdf.add("Nhân viên bán hàng:   Lê Đức nam");
//        pdf.add("DANH SÁCH SẢN PHẨM");
//        pdf.add("------- Cảm ơn quý khách -------");
//
//
//
////        Forn chữ
//        Font font1 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18,Font.BOLD);
//        Font font2 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,12,Font.BOLD);
//        Font font3 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,16,Font.BOLD);
//        Font font4 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,12,Font.HELVETICA);
//        Font font5 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,13,Font.BOLD);
//
//
////         header
//        p = new Paragraph(pdf.get(0), font1);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//        p = new Paragraph(pdf.get(1), font2);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//        p = new Paragraph(pdf.get(2), font3);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//        p = new Paragraph(pdf.get(3) +"\n\n");
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//        p = new Paragraph(pdf.get(4) +"\n" + pdf.get(5)+"\n" + pdf.get(6)+"\n" + pdf.get(7)+"\n" + pdf.get(8) +"\n\n",font4);
//        document.add(p);
//        p = new Paragraph(pdf.get(9) +"\n",font5);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//// Table
//
//
//        PdfPCell cell = new PdfPCell();
//        cell.setBackgroundColor(Color.LIGHT_GRAY);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//        cell.setPadding(5);
//// setAbsolutePosition() và setFixedHeight() tránh tràn trang
//        cell.setPhrase(new Phrase("STT"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Sản phẩm"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Số lượng"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Đơn giá"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Khuyến mại"));
//        table.addCell(cell);
//        cell.setPhrase(new Phrase("Thành tiền"));
//        table.addCell(cell);
//        writeTableData(table, list);
//        document.add(table);
//
////        footer
//
//        p = new Paragraph("\n\n"+pdf.get(10) +"\n",font4);
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//    }
//
//    private void writeTableData(PdfPTable table, List<SinhVien> list) {
//        for (SinhVien user : list) {
//            table.addCell(user.getMa());
//            table.addCell(user.getTên());
//            table.addCell(user.getTuổi().toString());
//        }
//    }
//
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
