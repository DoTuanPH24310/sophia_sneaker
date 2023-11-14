package com.example.sneaker_sophia.controller;


import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.request.NhanVienRequest;
import com.example.sneaker_sophia.service.*;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

// Sửa công tắc và phí ship.
// Thông báo ở modal tìm khách hàng

@Controller
@RequestMapping("/admin/tai-quay")
public class TaiQuayController {
    public static String tempIdHD = "";
    public static UUID tempIdCTSP;
    public static String tempIdKH = "";
    public static String tempIdDC = "";
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

    @Resource(name = "diaChiService")
    DiaChiService diaChiService;

    @Resource(name = "htttService")
    HTTTService htttService;

    @Resource(name = "anhRepository")
    AnhRepository anhRepository;
    //    alo ôla
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
            return "forward:/admin/tai-quay/open-soluong/" + idCTG;
        }
        if (hoaDonChiTietOld != null && hoaDonChiTietOld.getSoLuong() + soLuong > chiTietGiay.getSoLuong() + hoaDonChiTietOld.getSoLuong()) {
            return "forward:/admin/tai-quay/open-soluong/" + idCTG;

        }
        if (hoaDonChiTietServive.getHDCTByIdCTSP(chiTietGiay.getId(), tempIdHD) != null) {
            hoaDonChiTiet.setId(hoaDonChiTietOld.getId());
            hoaDonChiTiet.setSoLuong(hoaDonChiTietOld.getSoLuong() + soLuong);
        } else {
            hoaDonChiTiet.setSoLuong(soLuong);
        }
//        IdHoaDonCT idHoaDonCT = new IdHoaDonCT();
//        idHoaDonCT.setHoaDon(hoaDonService.getHoaDonById(tempIdHD));
//        idHoaDonCT.setChiTietGiay(chiTietGiay);

        hoaDonChiTiet.setTrangThai(1);
        hoaDonChiTiet.setDonGia(chiTietGiay.getGia());
//        hoaDonChiTiet.setIdHoaDonCT(idHoaDonCT);
        hoaDonChiTiet.setHoaDon(hoaDonService.getHoaDonById(tempIdHD));
        hoaDonChiTiet.setChiTietGiay(chiTietGiay);
        chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - soLuong);
        chiTietGiayService.save(chiTietGiay);
        hoaDonChiTietServive.addhdct(hoaDonChiTiet);
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    @GetMapping("/addHD")
    public String addHD(
            Model model
    ) {
        HoaDon hd = hoaDonService.addHD(model);
        if (hd != null) {
            tempIdHD = hd.getId();
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }

        return "redirect:/admin/tai-quay/hien-thi";
    }


    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable("id") String id,
            Model model,
            HttpSession session
    ) {
        tempIdHD = id;
        session.setAttribute("idHoaDon", id);
        session.setAttribute("checkBill", true);
        System.out.println(session.getAttribute("mySessionAttribute"));
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        model.addAttribute("maHD", id);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(id);
        Map<UUID, String> avtctgMap = new HashMap<>();
        List<ChiTietGiay> chiTietGiayList = chiTietGiayService.getAllCTG();

        for (ChiTietGiay ctg : chiTietGiayList){
            UUID idct = ctg.getId();
            String avtct = anhRepository.getAnhChinhByIdctg(idct);
            avtctgMap.put(idct, avtct);
            session.setAttribute("avtctsp", avtctgMap);
        }
        for (HoaDonChiTiet hdct : listhdct){
            UUID idctg = hdct.getChiTietGiay().getId();
            String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
            avtctgMap.put(idctg, avtctg);
            model.addAttribute("avtctgMap", avtctgMap);
        }

        model.addAttribute("listhdct", listhdct);
        Double tongTien = hoaDonChiTietServive.tongTienHD(tempIdHD);
        model.addAttribute("tongTienHD", tongTien);
        // 13-11
        session.setAttribute("tongTienHD", tongTien);
        // 30/10
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);

//        if(hoaDon.getTaiKhoan() != null){
//            session.setAttribute("loaihdg", hoaDon.getLoaiHoaDon());
//        }
        if (hoaDon.getTaiKhoan() != null) {
            session.setAttribute("idkh", hoaDon.getTaiKhoan().getId());
            session.setAttribute("countDC", diaChiService.getCountDiaChi(hoaDon.getTaiKhoan().getId()));
            tempIdKH = hoaDon.getTaiKhoan().getId();
            NhanVienRequest nhanVienRequest = taiKhoanService.getTaiKhoanById(hoaDon.getTaiKhoan().getId());
            DiaChi diaChiGH = diaChiService.findListTKByIdKHAndDCMD(tempIdKH);
            model.addAttribute("diaChiGH", diaChiGH);
            model.addAttribute("nhanVienRequest", nhanVienRequest);
            session.setAttribute("tinh", diaChiGH.getTinh());
            session.setAttribute("quan", diaChiGH.getQuanHuyen());
            session.setAttribute("phuong", diaChiGH.getPhuongXa());
            session.setAttribute("loaihdg", hoaDon.getLoaiHoaDon());
        } else {
            session.setAttribute("idkh", null);
            session.setAttribute("loaihdg", hoaDon.getLoaiHoaDon());
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
        if (hoaDon.getListHoaDonChiTiet().size() > 0) {
            tempIdHD = id;
            session.setAttribute("errTaiQuay", "Không thể xóa hóa đơn đã thêm sản phẩm");
            return "forward:/admin/tai-quay/detail/" + tempIdHD;
        }
        hoaDonService.deleteHD(hoaDon);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "redirect:/admin/tai-quay/hien-thi";
    }


    @GetMapping("/deletehdct/{idctsp}")
    public String deletehdct(
            @PathVariable("idctsp") UUID idctsp,
            Model model
    ) {
        hoaDonChiTietServive.deleteHDCT(idctsp, tempIdHD);
        session.setAttribute("errTaiQuay","Xóa thành công");
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
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


    @GetMapping("updatelhd")
    public String updateLoaiHDGH() {
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        if (hoaDon.getTaiKhoan() != null) {
            hoaDon.setLoaiHoaDon(2);
            hoaDonService.savehd(hoaDon);
        }

        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    @GetMapping("updatelhtq")
    public String updateLoaiHDTQ() {
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);

        hoaDon.setLoaiHoaDon(1);
        hoaDonService.savehd(hoaDon);


        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    // 31/10
    @GetMapping("updateDCMD/{id}")
    public String updateDCMD(
            @PathVariable("id") String iddc
    ) {
        tempIdDC = iddc;
        DiaChi diaChiThuong = diaChiService.getDiaChiById(iddc);
        DiaChi diaChiMD = diaChiService.getDiaChiByIdTaiKhoan(tempIdKH);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiService.saveDC(diaChiMD);
        }
        if (diaChiThuong.getDiaChiMacDinh() == 0) {
            diaChiThuong.setDiaChiMacDinh(1);
            diaChiService.saveDC(diaChiThuong);
        }
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }


    @GetMapping("deleteDC/{id}")
    public String delete(
            @PathVariable("id") String iddc
    ) {
        diaChiService.deleteById(iddc);
        session.setAttribute("errTaiQuay","Xóa thành công");
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }

    @GetMapping("addhttt")
    public String addhttt(
            Model model,
            @RequestParam("phuongThuc") Integer phuongThuc,
            @RequestParam("tienKhachDua") String tienKhachDua,
            @RequestParam("xa") String xa,
            @RequestParam("quan") String quan,
            @RequestParam("tinh") String tinh,
            @RequestParam("ghiChu") String ghiChu,
            @RequestParam(value = "phiVanChuyen", defaultValue = "0") String phiVanChuyen
    ) {
        HinhThucThanhToan hinhThucThanhToan = new HinhThucThanhToan();
        if (tienKhachDua.equals("")) {
            model.addAttribute("errTienKH", "Chưa trả tiền tao");
            return "forward:/admin/tai-quay/detail/" + tempIdHD;
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        NhanVienRequest taiKhoan = taiKhoanService.getTaiKhoanById(tempIdKH);
        Double tongTien = hoaDonChiTietServive.tongTienHD(tempIdHD);
        hinhThucThanhToan.setHoaDon(hoaDon);
        hinhThucThanhToan.setTrangThai(phuongThuc);
        hinhThucThanhToan.setSoTien(Double.parseDouble(tienKhachDua));
        htttService.savehttt(hinhThucThanhToan);
        hoaDon.setTenKhachHang(taiKhoan.getTen());
        hoaDon.setSoDienThoai(taiKhoan.getSdt());
        hoaDon.setDiaChi(taiKhoan.getDiaChiCuThe() + "," + xa + "," + quan + "," + tinh);
        if (hoaDon.getLoaiHoaDon() == 2){
            hoaDon.setPhiShip(Double.parseDouble(phiVanChuyen));
        }
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(tongTien);
        hoaDon.setTrangThai(1);
        hoaDonService.savehd(hoaDon);
        return "forward:/admin/tai-quay/hien-thi";
    }

    @PostMapping("adddc")
    public String adddc(
            @RequestParam("xa") Integer xa,
            @RequestParam("quan") Integer quan,
            @RequestParam("tinh") Integer tinh,
            @RequestParam("dcCuThe") String dcCuThe,
            @RequestParam("hoTen") String hoTen,
            @RequestParam("sdt") String sdt
    ) {
        List<DiaChi> listDC = diaChiService.findListTKById(tempIdKH);
        DiaChi diaChi = new DiaChi();
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanByIdKH(tempIdKH);
        DiaChi diaChiMD = diaChiService.getDiaChiByIdTaiKhoan(tempIdKH);
        if (diaChiMD.getDiaChiMacDinh() == 1) {
            diaChiMD.setDiaChiMacDinh(0);
            diaChiService.saveDC(diaChiMD);
        }
        diaChi.setTaiKhoan(taiKhoan);
        diaChi.setPhuongXa(xa);
        diaChi.setQuanHuyen(quan);
        diaChi.setTinh(tinh);
        diaChi.setDiaChiCuThe(dcCuThe);
        diaChi.setTen(hoTen);
        diaChi.setSdt(sdt);
        diaChi.setDiaChiMacDinh(1);
        diaChiService.saveDC(diaChi);
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
    }

    // 11-11
    @PostMapping(value = "/scan", consumes = "application/json")
    public String handleQRCode(@RequestBody Map<String, String> requestBody) {
        String qrCodeData = requestBody.get("qrCodeData");

        ChiTietGiay chiTietGiay = chiTietGiayService.getCTGByQrCode(qrCodeData);
        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        HoaDonChiTiet hoaDonChiTietOld = hoaDonChiTietServive.getHDCTByIdCTSP(chiTietGiay.getId(), tempIdHD);

        if(chiTietGiay.getSoLuong() == 0){
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }

        if (hoaDonChiTietServive.getHDCTByIdCTSP(chiTietGiay.getId(), tempIdHD) != null) {
            hoaDonChiTiet.setId(hoaDonChiTietOld.getId());
            hoaDonChiTiet.setSoLuong(hoaDonChiTietOld.getSoLuong() + 1);
        } else {
            hoaDonChiTiet.setSoLuong(1);
        }

        hoaDonChiTiet.setDonGia(chiTietGiay.getGia());
        hoaDonChiTiet.setHoaDon(hoaDonService.getHoaDonById(tempIdHD));
        hoaDonChiTiet.setChiTietGiay(chiTietGiay);
        hoaDonChiTiet.setTrangThai(1);
        chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - 1);
        chiTietGiayService.save(chiTietGiay);
        hoaDonChiTietServive.addhdct(hoaDonChiTiet);
        // Process qrCodeData as needed
//        System.out.println("Received QR Code data: " + qrCodeData);
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
//        return "redirect:/scan"; // Redirect to the home page or any other desired page
    }

    // 13-11

    @GetMapping("deletekhhd")
    public String deletekhhd(){
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        hoaDon.setTaiKhoan(null);
        hoaDon.setLoaiHoaDon(1);
        hoaDonService.savehd(hoaDon);
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
    }


    // =================================================================
//    @GetMapping("/pdf")
//    public void pdf(HttpServletResponse response) throws IOException {
//        SinhVien sv = new SinhVien("1", "Giày riu (32, Đỏ)", 10);
//        SinhVien sv1 = new SinhVien("100.000", "20.000", 80000);
//        SinhVien sv2 = new SinhVien("Sv03", "nam", 200000000);
//        List<SinhVien> list = new ArrayList<>(List.of(sv, sv1, sv2));
//
////        Để thiết lập cho trình duyệt
//        response.setContentType("application/pdf");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users.pdf";
//        response.setHeader(headerKey, headerValue);
//        export(response, list);
//
//    }
//
//    public void export(HttpServletResponse response, List<SinhVien> list) throws IOException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, response.getOutputStream());
//        document.open();
//        PdfPTable table = new PdfPTable(6);
//        table.setWidthPercentage(100f);
//        table.setWidths(new float[]{1.2f, 4f, 2f, 3.5f, 3.5f, 3.5f});
//        table.setSpacingBefore(15);
//        addContent(document, table, list);
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
////        Forn chữ
//        Font font1 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.BOLD);
//        Font font2 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD);
//        Font font3 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD);
//        Font font4 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.HELVETICA);
//        Font font5 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 13, Font.BOLD);
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
//        p = new Paragraph(pdf.get(3) + "\n\n");
//        p.setAlignment(Paragraph.ALIGN_CENTER);
//        document.add(p);
//        p = new Paragraph(pdf.get(4) + "\n" + pdf.get(5) + "\n" + pdf.get(6) + "\n" + pdf.get(7) + "\n" + pdf.get(8) + "\n\n", font4);
//        document.add(p);
//        p = new Paragraph(pdf.get(9) + "\n", font5);
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
//        p = new Paragraph("\n\n" + pdf.get(10) + "\n", font4);
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