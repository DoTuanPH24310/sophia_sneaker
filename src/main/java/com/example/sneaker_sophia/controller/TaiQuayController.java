package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.dto.IdHoaDonCT;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.request.NhanVienRequest;
import com.example.sneaker_sophia.service.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
// reset sesion khi thanh toán thành công

@Controller
@RequestMapping("/admin/tai-quay")
public class TaiQuayController {
    public static String tempIdHD = "";
    public static UUID tempIdCTSP;
    public static String tempIdKH = "";
    // trạnh thái = 2 (chờ)tai-quay
    @Resource(name = "hoaDonService")
    HoaDonService hoaDonService;

    @Resource(name = "hoaDonChiTietServive")
    HoaDonChiTietServive hoaDonChiTietServive;

    @Autowired
    private HttpSession session;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;


    @GetMapping("/hien-thi")
    public String index(Model model) {
        tempIdHD = "";
        session.setAttribute("checkBill", false);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        session.setAttribute("listhdct", new ArrayList<>());
        return "/admin/taiquay/index";
    }


    @GetMapping("/open-sanpham")
    public String showModal(Model model) {
//        if (tempIdHD.isEmpty()) {
//            model.addAttribute("errShowModal", "Phải chọn HĐ trước thằng ngu");
//            return "forward:/admin/tai-quay/hien-thi";
//        }
//        List<ChiTietGiay> listCTG = chiTietGiayService.findAllByTrangThaiEquals(0);
//        model.addAttribute("loaiGiayList", loaiGiayService.findByTrangThaiEquals(0));
//        model.addAttribute("mauSacList", mauSacService.findByTrangThaiEquals(0));
//        model.addAttribute("kichCoList", kichCoService.findByTrangThaiEquals(0));
//        model.addAttribute("giayList", giayService.findAllByTrangThaiEquals(0));
//        model.addAttribute("hangList", hangService.findByTrangThaiEquals(0));
//        model.addAttribute("deList", deGiayService.findByTrangThaiEquals(0));
//        model.addAttribute("listCTG", listCTG);
        model.addAttribute("modalSanPham", true);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    //    Bạt ở đây nha thằng ngiu
    @GetMapping("/open-soluong/{id}")
    public String showSoLuong(
            Model model,
            @PathVariable("id") UUID idctsp

    ) {

        tempIdCTSP = idctsp;
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idctsp);
        model.addAttribute("soLuongTon", chiTietGiay.getSoLuong());
        model.addAttribute("modalSanPham", true);
        model.addAttribute("modalSoLuong", true);
        return "forward:/admin/tai-quay/open-sanpham";
    }

    //28-10
    @GetMapping("addhdct")
    public String addhdct(
            @RequestParam("soLuong") Integer soLuong,
            @RequestParam("maCTG") String maCTG,
            Model model
    ) {
        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        UUID idCTG = chiTietGiayService.getIdCTGByMa(maCTG);
        HoaDonChiTiet hoaDonChiTietOld = hoaDonChiTietServive.getHDCTByIdCTSP(idCTG, tempIdHD);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idCTG);
        if (hoaDonChiTietOld == null && soLuong > chiTietGiay.getSoLuong()) {
            model.addAttribute("errSLT", "Không đủ số lượng tồn");
            return "forward:/admin/tai-quay/open-soluong/" + idCTG;
        }
        if (hoaDonChiTietOld != null && hoaDonChiTietOld.getSoLuong() + soLuong > chiTietGiay.getSoLuong() + hoaDonChiTietOld.getSoLuong()) {
            model.addAttribute("errSLT", "Không đủ số lượng tồn");
            return "forward:/admin/tai-quay/open-soluong/" + idCTG;

        }
        if (hoaDonChiTietServive.getHDCTByIdCTSP(chiTietGiay.getId(), tempIdHD) != null) {
            hoaDonChiTiet.setSoLuong(hoaDonChiTietOld.getSoLuong() + soLuong);
        } else {
            hoaDonChiTiet.setSoLuong(soLuong);
        }
        IdHoaDonCT idHoaDonCT = new IdHoaDonCT();
        idHoaDonCT.setHoaDon(hoaDonService.getHoaDonById(tempIdHD));
        idHoaDonCT.setChiTietGiay(chiTietGiay);

        hoaDonChiTiet.setTrangThai(1);
        hoaDonChiTiet.setDonGia(chiTietGiay.getGia());
        hoaDonChiTiet.setIdHoaDonCT(idHoaDonCT);
        chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - soLuong);
        chiTietGiayService.save(chiTietGiay);
        hoaDonChiTietServive.addhdct(hoaDonChiTiet);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    @RequestMapping("/addHD")
    public String addHD(
            Model model
    ) {
        HoaDon hd = hoaDonService.addHD(model);
        if (hd != null) {
            tempIdHD = hd.getId();
            return "forward:/admin/tai-quay/detail/" + tempIdHD;
        }

        return "forward:/admin/tai-quay/hien-thi";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable("id") String id,
            Model model
    ) {
        tempIdHD = id;
        session.setAttribute("idHoaDon", id);
        session.setAttribute("checkBill", true);
        System.out.println(session.getAttribute("mySessionAttribute"));
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        model.addAttribute("maHD", id);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(id);
        model.addAttribute("listhdct", listhdct);
        Double tongTien = hoaDonChiTietServive.tongTienHD(tempIdHD);
        model.addAttribute("tongTienHD", tongTien);
        // 30/10
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon.getTaiKhoan() != null) {
            session.setAttribute("idkh", hoaDon.getTaiKhoan().getId());
            NhanVienRequest nhanVienRequest = taiKhoanService.getTaiKhoanById(hoaDon.getTaiKhoan().getId());
            model.addAttribute("nhanVienRequest", nhanVienRequest);
            session.setAttribute("tinh", nhanVienRequest.getTinh());
            session.setAttribute("quan", nhanVienRequest.getQuanHuyen());
            session.setAttribute("phuong", nhanVienRequest.getPhuongXa());
        } else {
            return "/admin/taiquay/index";
        }
        return "/admin/taiquay/index";
    }


    @GetMapping("deletehd/{id}")
    public String deleteHD(
            @PathVariable("id") String id,
            Model model
    ) {
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon.getListHoaDonChiTiet().size() > 1) {
            tempIdHD = id;
            model.addAttribute("errHD", "Hóa đơn vẫn còn sản phẩm");
            return "forward:/admin/tai-quay/detail/" + tempIdHD;
        }
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
        hoaDonChiTietServive.deleteHDCT(idctsp, tempIdHD);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }


    @GetMapping("/update-down/{id}")
    public String updateDownSLHDCT(
            Model model,
            @PathVariable("id") UUID idctsp
    ) {
        hoaDonChiTietServive.updateDownSLHDCT(idctsp, tempIdHD, model);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    @GetMapping("/update-up/{id}")
    public String updateUpSLHDCT(
            Model model,
            @PathVariable("id") UUID idctsp
    ) {
        hoaDonChiTietServive.updateUpSLHDCT(idctsp, tempIdHD, model);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    //    30/10
    @GetMapping("chonTK/{id}")
    public String chonTK(
            Model model, @PathVariable("id") String idkh
            , HttpSession session
    ) {
//        session.setAttribute("idkh", idkh);
        NhanVienRequest nhanVienRequest = taiKhoanService.getTaiKhoanById(idkh);
        model.addAttribute("nhanVienRequest", nhanVienRequest);
        session.setAttribute("tinh", nhanVienRequest.getTinh());
        session.setAttribute("quan", nhanVienRequest.getQuanHuyen());
        session.setAttribute("phuong", nhanVienRequest.getPhuongXa());
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        hoaDon.setTaiKhoan(TaiKhoan.builder().id(idkh).build());
        hoaDonService.savehd(hoaDon);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;

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