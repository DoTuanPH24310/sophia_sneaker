package com.example.sneaker_sophia.controller.manage;

import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.AnhRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.example.sneaker_sophia.service.*;
import com.example.sneaker_sophia.validate.AlertInfo;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

// Sửa lại tổng tiền hóa đơn(1 là dùng cách api,2 là seesion)
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

    @Resource(name = "kmService")
    KMService kmService;

    @Resource(name = "lshdService")
    LSHDService lshdService;

    @Resource(name = "taiQuayService")
    TaiQuayService taiQuayService;

    @Autowired
    private AlertInfo alertInfo;

    //    alo ôla
    @GetMapping("/hien-thi")
    public String index(Model model) {
        tempIdHD = "";
        session.setAttribute("checkBill", false);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        session.setAttribute("listhdct", new ArrayList<>());
        if (session.getAttribute("checkTTHT") != null) {
            session.setAttribute("checkTTHT", "1");
        }
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
            @RequestParam(value = "soLuong", required = false) String soLuong1,
            @RequestParam(value = "maCTG", required = false) String maCTG,
            Model model
    ) {
        int soLuong;
        try {
            soLuong = Integer.parseInt(soLuong1);
            if (soLuong <= 0) {
                alertInfo.alert("errTaiQuay",null);
                return "redirect:/admin/tai-quay/detail/" + tempIdHD;
            }
        } catch (NumberFormatException e) {
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }

        HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
        UUID idCTG = chiTietGiayService.getIdCTGByMa(maCTG);
        if(idCTG == null){
            alertInfo.alert("errTaiQuay",null);
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }
        HoaDonChiTiet hoaDonChiTietOld = hoaDonChiTietServive.getHDCTByIdCTSP(idCTG, tempIdHD);
        ChiTietGiay chiTietGiay = chiTietGiayService.getChiTietGiayByIdctg(idCTG);
        if (hoaDonChiTietOld == null && soLuong > chiTietGiay.getSoLuong()) {
            alertInfo.alert("errTaiQuay","Không đủ số lượng tồn");
            return "redirect:/admin/tai-quay/open-soluong/" + idCTG;
        }
        if (hoaDonChiTietOld != null && hoaDonChiTietOld.getSoLuong() + soLuong > chiTietGiay.getSoLuong() + hoaDonChiTietOld.getSoLuong()) {
            alertInfo.alert("errTaiQuay","Không đủ số lượng tồn");
            return "redirect:/admin/tai-quay/open-soluong/" + idCTG;

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


        // giam slt trong km
        List<Voucher> voucherList = kmService.getAllKMByIdctg(idCTG);


        if (chiTietGiayService.tongKMByIdctg(idCTG) != null) {
            hoaDonChiTiet.setPhanTramGiam(chiTietGiayService.tongKMByIdctg(idCTG));
        } else {
            if (hoaDonChiTietOld != null) {
                hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam());
                hoaDonChiTiet.setPhanTramGiam(hoaDonChiTietOld.getPhanTramGiam());
            } else {
                hoaDonChiTiet.setSoLuongGiam(0);
                hoaDonChiTiet.setPhanTramGiam(0);
            }
        }


        for (Voucher voucher : voucherList) {
            if (voucher.getSoLuong() > 0) {
                if (soLuong >= voucher.getSoLuong()) {
                    if (hoaDonChiTietOld != null) {
                        hoaDonChiTiet.setSoLuongGiam(voucher.getSoLuong() + hoaDonChiTietOld.getSoLuongGiam());
                        voucher.setSoLuong(0);
                    } else {
                        hoaDonChiTiet.setSoLuongGiam(voucher.getSoLuong());
                        voucher.setSoLuong(0);
                    }

                } else {

                    if (hoaDonChiTietOld != null) {
                        hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam() + soLuong);
                        voucher.setSoLuong(voucher.getSoLuong() - soLuong);
                    } else {
                        hoaDonChiTiet.setSoLuongGiam(soLuong);
                        voucher.setSoLuong(voucher.getSoLuong() - soLuong);
                    }

                }
            } else {
                if (hoaDonChiTietOld != null) {
                    hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam());
                } else {
                    hoaDonChiTiet.setSoLuongGiam(0);
                }

            }

            kmService.saveVC(voucher);

        }

        hoaDonChiTiet.setDonGia(chiTietGiay.getGia());
//        hoaDonChiTiet.setIdHoaDonCT(idHoaDonCT);
        hoaDonChiTiet.setHoaDon(hoaDonService.getHoaDonById(tempIdHD));
        hoaDonChiTiet.setChiTietGiay(chiTietGiay);
        chiTietGiay.setSoLuong(chiTietGiay.getSoLuong() - soLuong);
        hoaDonChiTiet.setTrangThai(1);
        chiTietGiayService.save(chiTietGiay);
        hoaDonChiTietServive.addhdct(hoaDonChiTiet);
        alertInfo.alert("successTaiQuay","Sản phẩm đã được thêm");
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
    }

    @GetMapping("/addHD")
    public String addHD(
            Model model
    ) {
        HoaDon hd = hoaDonService.addHD(model);
        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hd);
        lichSuHoaDon.setPhuongThuc("2");
        lshdService.savelshd(lichSuHoaDon);
        if (hd != null) {
            tempIdHD = hd.getId();
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }

        return "redirect:/admin/tai-quay/hien-thi";
    }


    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable(value = "id", required = false) String id,
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
        Double tongTien = 0.0;
        for (ChiTietGiay ctg : chiTietGiayList) {
            UUID idct = ctg.getId();
            String avtct = anhRepository.getAnhChinhByIdctg(idct);
            avtctgMap.put(idct, avtct);
            session.setAttribute("avtctsp", avtctgMap);
        }
        for (HoaDonChiTiet hdct : listhdct) {
            UUID idctg = hdct.getChiTietGiay().getId();
            String avtctg = anhRepository.getAnhChinhByIdctg(idctg);
            tongTien += (hdct.getDonGia() * (1 - ((hdct.getPhanTramGiam()) / 100.0)) * hdct.getSoLuongGiam()) +
                    (hdct.getDonGia() * (hdct.getSoLuong() - hdct.getSoLuongGiam()));
            avtctgMap.put(idctg, avtctg);

            model.addAttribute("avtctgMap", avtctgMap);
        }

        model.addAttribute("listhdct", listhdct);
//        Double tongTien = hoaDonChiTietServive.tongTienHD(tempIdHD);
        model.addAttribute("tongTienHD", tongTien);
        // 13-11
        session.setAttribute("tongTienHD", tongTien);
        // 30/10

        // 18/11
        Double tongTienTruocGiam = hoaDonChiTietServive.tongTienTruocGiam(id);
        Double tienGiam = hoaDonChiTietServive.tienGiam(id) == null ? 0 : hoaDonChiTietServive.tienGiam(id);
        // 20/11
        HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(id);
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hinhThucThanhToan != null) {
            model.addAttribute("tienKhachDua", hinhThucThanhToan.getSoTien());
            model.addAttribute("phiVanChuyen", hoaDon.getPhiShip());
            model.addAttribute("httt",hinhThucThanhToan.getTrangThai());
        }

        model.addAttribute("tongTienTruocGiam", tongTienTruocGiam);
        model.addAttribute("tienGiam", tienGiam);


        if (hoaDon != null) {
            hoaDon.setTongTien(tongTien);
            hoaDon.setKhuyenMai(tienGiam);
            hoaDonService.savehd(hoaDon);
        }


        if (hoaDon.getTaiKhoan() != null) {
            session.setAttribute("idkh", hoaDon.getTaiKhoan().getId());
            session.setAttribute("countDC", diaChiService.getCountDiaChi(hoaDon.getTaiKhoan().getId()));
            tempIdKH = hoaDon.getTaiKhoan().getId();
            TaiKhoanRequest nhanVienRequest = taiKhoanService.getTaiKhoanById(hoaDon.getTaiKhoan().getId());
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
            @PathVariable(value = "id", required = false) String id,
            @RequestParam(value = "value", required = false) String liDoHuy,
            Model model
    ) {
        if (liDoHuy.length() > 20){
            alertInfo.alert("errTaiQuay","Tối đa 20 kí tự");
            return "redirect:/admin/tai-quay/hien-thi";
        }
        HoaDon hoaDon = hoaDonService.getHoaDonById(id);
        if (hoaDon.getListHoaDonChiTiet().size() > 0) {
            tempIdHD = id;
            alertInfo.alert("errTaiQuay","Không thể hủy hóa đơn đã thêm sản phẩm");
            return "forward:/admin/tai-quay/detail/" + tempIdHD;
        }

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setPhuongThuc("6");
        lshdService.savelshd(lichSuHoaDon);
        hoaDon.setTrangThai(6);
        hoaDon.setGhiChu(liDoHuy);
        hoaDonService.savehd(hoaDon);
        List<HoaDon> list = hoaDonService.getHoaDonByTrangThai();
        model.addAttribute("listHDC", list);
        alertInfo.alert("successTaiQuay","Hóa đơn đã được hủy");
        return "redirect:/admin/tai-quay/hien-thi";
    }


    @GetMapping("/deletehdct/{idctsp}")
    public String deletehdct(
            @PathVariable("idctsp") UUID idctsp,
            Model model
    ) {
        hoaDonChiTietServive.deleteHDCT(idctsp, tempIdHD);
        alertInfo.alert("successTaiQuay","Sản phẩm đã được xóa");
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
            Model model, @PathVariable(value = "id", required = false) String idkh
            , HttpSession session
    ) {
//        session.setAttribute("idkh", idkh);
        TaiKhoanRequest nhanVienRequest = taiKhoanService.getTaiKhoanById(idkh);
        model.addAttribute("nhanVienRequest", nhanVienRequest);
        session.setAttribute("tinh", nhanVienRequest.getTinh());
        session.setAttribute("quan", nhanVienRequest.getQuanHuyen());
        session.setAttribute("phuong", nhanVienRequest.getPhuongXa());
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        hoaDon.setTaiKhoan(TaiKhoan.builder().id(idkh).build());
        hoaDon.setTenKhachHang(nhanVienRequest.getTen());
        hoaDon.setSoDienThoai(nhanVienRequest.getSdt());
        hoaDonService.savehd(hoaDon);
        alertInfo.alert("successTaiQuay","Khách hàng đã được thêm");
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
        alertInfo.alert("successTaiQuay","Địa chỉ đã được thay đổi");
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
    }


    @GetMapping("deleteDC/{id}")
    public String delete(
            @PathVariable(value = "id", required = false) String iddc
    ) {
        diaChiService.deleteById(iddc);
        alertInfo.alert("errTaiQuay","Xóa thành công");
        return "forward:/admin/tai-quay/detail/" + tempIdHD;
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

        if (chiTietGiay.getSoLuong() == 0) {
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }

        if (hoaDonChiTietServive.getHDCTByIdCTSP(chiTietGiay.getId(), tempIdHD) != null) {
            hoaDonChiTiet.setId(hoaDonChiTietOld.getId());
            hoaDonChiTiet.setSoLuong(hoaDonChiTietOld.getSoLuong() + 1);
        } else {
            hoaDonChiTiet.setSoLuong(1);
        }
        if (chiTietGiayService.tongKMByIdctg(chiTietGiay.getId()) != null) {
            hoaDonChiTiet.setPhanTramGiam(chiTietGiayService.tongKMByIdctg(chiTietGiay.getId()));
        } else {
            if (hoaDonChiTietOld != null) {
                hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam());
                hoaDonChiTiet.setPhanTramGiam(hoaDonChiTietOld.getPhanTramGiam());
            } else {
                hoaDonChiTiet.setSoLuongGiam(0);
                hoaDonChiTiet.setPhanTramGiam(0);
            }
        }
        List<Voucher> voucherList = kmService.getAllKMByIdctg(chiTietGiay.getId());
        for (Voucher voucher : voucherList) {
            if (voucher.getSoLuong() > 0) {
                if (1 >= voucher.getSoLuong()) {
                    if (hoaDonChiTietOld != null) {
                        hoaDonChiTiet.setSoLuongGiam(voucher.getSoLuong() + hoaDonChiTietOld.getSoLuongGiam());
                        voucher.setSoLuong(0);
                    } else {
                        hoaDonChiTiet.setSoLuongGiam(voucher.getSoLuong());
                        voucher.setSoLuong(0);
                    }

                } else {

                    if (hoaDonChiTietOld != null) {
                        hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam() + 1);
                        voucher.setSoLuong(voucher.getSoLuong() - 1);
                    } else {
                        hoaDonChiTiet.setSoLuongGiam(1);
                        voucher.setSoLuong(voucher.getSoLuong() - 1);
                    }

                }
            } else {
                if (hoaDonChiTietOld != null) {
                    hoaDonChiTiet.setSoLuongGiam(hoaDonChiTietOld.getSoLuongGiam());
                } else {
                    hoaDonChiTiet.setSoLuongGiam(0);
                }

            }

            kmService.saveVC(voucher);

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
    public String deletekhhd() {
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        hoaDon.setTaiKhoan(null);
        hoaDon.setLoaiHoaDon(1);
        hoaDonService.savehd(hoaDon);
        return "redirect:/admin/tai-quay/detail/" + tempIdHD;
    }

    @PostMapping("addhttt")
    public String addhttt(
            Model model,
            @RequestParam(value = "phuongThuc", required = false) Integer phuongThuc,
            @RequestParam(value = "tienKhachDua", required = false, defaultValue = "0") String tienKhachDua,
            @RequestParam(value = "xa", required = false) String xa,
            @RequestParam(value = "quan", required = false) String quan,
            @RequestParam(value = "tinh", required = false) String tinh,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            @RequestParam(value = "tienDu", required = false) String tienDu,
            @RequestParam(value = "phiVanChuyen", defaultValue = "0", required = false) String phiVanChuyen,
            HttpServletResponse response
    ) throws IOException {
        HinhThucThanhToan hinhThucThanhToan = htttService.getHTTTByIdhd(tempIdHD);
        phiVanChuyen = phiVanChuyen.replaceAll("[^\\d]", "");
        tienKhachDua = tienKhachDua.replaceAll("[^\\d]", "");
        try {
            Double phiShip = Double.parseDouble(phiVanChuyen);
            if (phiShip < 0) {
                model.addAttribute("error", "Vui lòng phí vận chuyển > 0.");
                return "redirect:/admin/tai-quay/detail/" + tempIdHD;
            }
        } catch (NumberFormatException e) {
            model.addAttribute("error", "Vui lòng nhập phí vận chuyển không hợp lệ.");
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }
        Double tongTien = hoaDonChiTietServive.tongTienSauGiam(tempIdHD);
        HoaDon hoaDon = hoaDonService.getHoaDonById(tempIdHD);
        List<HoaDonChiTiet> listhdct = hoaDonChiTietServive.getHDCTByIdHD(tempIdHD);
        if(listhdct.size() == 0){
            System.out.println("Thanh toan that bai");
            return "redirect:/admin/tai-quay/detail/" + tempIdHD;
        }
        if (hinhThucThanhToan != null) {
            tienKhachDua = tienKhachDua.replaceAll("[^\\d]", "");
            hinhThucThanhToan.setSoTien(Double.parseDouble(tienKhachDua));
            htttService.savehttt(hinhThucThanhToan);
        } else {
            hinhThucThanhToan = new HinhThucThanhToan();
            hinhThucThanhToan.setHoaDon(hoaDon);
            hinhThucThanhToan.setTrangThai(phuongThuc);
            tienKhachDua = tienKhachDua.replaceAll("[^\\d]", "");
            hinhThucThanhToan.setSoTien(Double.parseDouble(tienKhachDua));
            htttService.savehttt(hinhThucThanhToan);

        }

        if (!tempIdKH.equals("") && hoaDon.getLoaiHoaDon() == 2) {
            TaiKhoanRequest taiKhoan = taiKhoanService.getTaiKhoanById(tempIdKH);
            if(!taiQuayService.validateAđhttt(xa, quan, tinh)){
                return "redirect:/admin/tai-quay/detail/" + tempIdHD;
            }
            hoaDon.setTenKhachHang(taiKhoan.getTen());
            hoaDon.setSoDienThoai(taiKhoan.getSdt());
            hoaDon.setDiaChi(taiKhoan.getDiaChiCuThe() + "," + xa + "," + quan + "," + tinh);
            phiVanChuyen = phiVanChuyen.replaceAll("[^\\d]", "");
            hoaDon.setPhiShip(Double.parseDouble(phiVanChuyen));
        }

//        if (hoaDon.getLoaiHoaDon() == 2){
//            phiVanChuyen = phiVanChuyen.replaceAll("[^\\d]", "");
//            hoaDon.setPhiShip(Double.parseDouble(phiVanChuyen));
//        }
        // lưu lại thông tin khi trả sau
        if (phuongThuc == 3) {
            hoaDon.setTienThua(0.0);
        } else {
            hoaDon.setTienThua(Double.parseDouble(tienKhachDua) - tongTien - Double.parseDouble(phiVanChuyen));
        }

        hoaDon.setKhuyenMai(hoaDonChiTietServive.tienGiam(tempIdHD));
        hoaDon.setGhiChu(ghiChu);
        hoaDon.setTongTien(tongTien);
        if (hoaDon.getLoaiHoaDon() == 2) {
            hoaDon.setTrangThai(4);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("4");
            lshdService.savelshd(lichSuHoaDon);
        } else {
            hoaDon.setTrangThai(1);
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setHoaDon(hoaDon);
            lichSuHoaDon.setPhuongThuc("1");
            lshdService.savelshd(lichSuHoaDon);
        }

        hoaDonService.savehd(hoaDon);
        session.setAttribute("checkTTHT", true);
        alertInfo.alert("successTaiQuay","Thanh toán thành công");
        return "redirect:/admin/tai-quay/hien-thi";
    }


//     =================================================================

    @GetMapping("/pdf/{idHD}")
    public void pdf(HttpServletResponse response, @PathVariable("idHD") String idHD) throws IOException {

//        Để thiết lập cho trình duyệt
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users.pdf";
        response.setHeader(headerKey, headerValue);
        export(response, idHD);
    }

    public void export(HttpServletResponse response, String idHD) throws IOException {

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{1.1f, 4.5f, 1.5f, 3.3f, 3.3f, 4.0f});
        table.setSpacingBefore(15);
        addContent(document, table, idHD);
        document.close();
    }

    public void addContent(Document document, PdfPTable table, String idHD) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        HoaDon hd = hoaDonService.getHoaDonById(idHD);
        if (hd.getTenKhachHang() == null) hd.setTenKhachHang(" ");
        if (hd.getDiaChi() == null) hd.setDiaChi(" ");
        if (hd.getSoDienThoai() == null) hd.setSoDienThoai(" ");
        Paragraph p;
        List<String> pdf = new ArrayList<>();
        pdf.add("SOPHIA-SNEAKER");
        pdf.add("\n\n Số điện thoại: 0123456789 \n" +
                "Email: hoangnhph24464@fpt.edu.vn \n" +
                "Địa chỉ: Trịnh Văn Bô - Nam Từ Liêm - Hà nội \n" +
                "Ngân hàng: MBBank - Số tài khoản: 0001541506626 \n" +
                "Chủ tài khoản: NGUYEN HUY HOANG \n");
        pdf.add("\nHÓA ĐƠN BÁN HÀNG");

//        3
        pdf.add("DANH SÁCH SẢN PHẨM");
        pdf.add("------- Cảm ơn quý khách -------");


//        Forn chữ
        Font font1 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 18, Font.BOLD);
        Font font2 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD);
        Font font3 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLD);
        Font font4 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.HELVETICA);
        Font font5 = FontFactory.getFont("Times New Roman", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 13, Font.BOLD);


//         header
        p = new Paragraph(pdf.get(0), font1);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        p = new Paragraph(pdf.get(1), font2);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        p = new Paragraph(pdf.get(2), font3);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);

        p = new Paragraph(hd.getMaHoaDOn() + "\n\n");
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        p = new Paragraph("Ngày mua:    " + formatter.format(hd.getCreatedDate()) + "\n\n" + "Khách hàng:    " + hd.getTenKhachHang() + "\n\n" + "Địa chỉ:    " +
                hd.getTenKhachHang() + "," + hd.getSoDienThoai() + "/ " + hd.getDiaChi() + "\n\n" + "Điện thoại:    " + hd.getSoDienThoai() + "\n\n" + "Người bán:    " + "Nguyễn Huy Hoàng" + "\n\n", font4);
        document.add(p);
        p = new Paragraph(pdf.get(3) + "\n", font5);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);


        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
// setAbsolutePosition() và setFixedHeight() tránh tràn trang
        cell.setPhrase(new Phrase("STT"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Sản phẩm"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Số lượng"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Đơn giá"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Khuyến mại"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Thành tiền"));
        table.addCell(cell);
        writeTableData(table, idHD);
        document.add(table);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        p = new Paragraph("\n\n" + "Khuyến mại: " + currencyFormat.format(hoaDonChiTietServive.tienGiam(idHD)), font4);
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p);

        if (hd.getLoaiHoaDon() == 2) {
            p = new Paragraph("\n" + "Phí Ship: " + currencyFormat.format(hd.getPhiShip()), font4);
            p.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(p);
        }

        p = new Paragraph("\n" + "Tổng tiền: " + currencyFormat.format(hd.getTongTien()), font4);
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p);

        p = new Paragraph("\n" + "Tiền thừa: " + currencyFormat.format(hd.getTienThua()), font4);
        p.setAlignment(Paragraph.ALIGN_RIGHT);
        document.add(p);


        //        footer
        p = new Paragraph("\n\n" + pdf.get(4) + "\n", font4);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
    }

    private void writeTableData(PdfPTable table, String idHD) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        List<HoaDonChiTiet> list = hoaDonChiTietServive.getHDCTByIdHD(idHD);
        for (int i = 0; i < list.size(); i++) {
            Double donGia = list.get(i).getDonGia();
            Double km = ((list.get(i).getPhanTramGiam() / 100.0) * list.get(i).getSoLuongGiam() * list.get(i).getDonGia());
            Double thanhTien = ((list.get(i).getDonGia() * (1 - ((list.get(i).getPhanTramGiam()) / 100.0)) * list.get(i).getSoLuongGiam()) +
                    (list.get(i).getDonGia() * (list.get(i).getSoLuong() - list.get(i).getSoLuongGiam())));
            table.addCell((i + 1) + "\n");
            table.addCell("[" + list.get(i).getChiTietGiay().getHang().getTen() + "]" + list.get(i).getChiTietGiay().getTen() + list.get(i).getChiTietGiay().getGiay().getTen() + "[" + list.get(i).getChiTietGiay().getMauSac().getTen() + "]" + "[" + list.get(i).getChiTietGiay().getKichCo().getTen() + "]" + "\n");
            table.addCell((list.get(i).getSoLuong()) + "\n");
            table.addCell(currencyFormat.format(donGia) + "\n");
            table.addCell(currencyFormat.format(km) + "\n");
            table.addCell(currencyFormat.format(thanhTien));
        }

    }


}