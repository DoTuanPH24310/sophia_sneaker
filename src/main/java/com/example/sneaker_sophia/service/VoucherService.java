package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.VoucherRepository;
import com.example.sneaker_sophia.validate.AlertInfo;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ServletRequest request;


    @Autowired
    private AlertInfo alertInfo;


    @Autowired
    private GiayService giayService;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    @Autowired
    private CTG_KhuyenMaiService chCtg_khuyenMaiService;


    public void addAttributeModel(Model model, List<String> listId, List<String> listIdCTG) {
        model.addAttribute("listId", listId);
        model.addAttribute("listIDCTG", listIdCTG);
        model.addAttribute("listGiay", giayService.findAllByTrangThaiEquals(0));
    }

    public List<Voucher> findByTrangThaiNotLike() {
        return voucherRepository.findByTrangThaiNotLike();
    }


    public Page locVaTimKiem(Pageable pageable, Model model) {
        Page page = null;
        String txtSearchReq = request.getParameter("textSearch");
        String trangThaiReq = request.getParameter("trangThai");
        String txtSearch = txtSearchReq == null || txtSearchReq.isBlank() ? null : "%" + txtSearchReq + "%";
        Integer check = trangThaiReq == null || trangThaiReq.isBlank() || trangThaiReq.equals("-1") ? null : Integer.parseInt(trangThaiReq);
        model.addAttribute("textSearch", txtSearchReq);
        model.addAttribute("trangThai", trangThaiReq);
        if (trangThaiReq != null && trangThaiReq.equals("-1") && txtSearchReq.trim().length() == 0) {
            return voucherRepository.findAllAndSort(pageable);
        }
        page = voucherRepository.locVaTimKiem(check, txtSearch, pageable);
        return page;
    }


    public boolean validate(VoucherDTO vc, Model model, List<String> listIDCTG) {
        int check = 0;
        String errTen = null, errGiaTri = null, errNBD = null, errNKT = null, err = null, errSoLuong = null;
        Integer ptg = 0;
        Integer sl = 0;
        Integer nbt = 0;
        LocalDateTime timeNow = LocalDateTime.now().minusMinutes(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (vc.getTen() == null || vc.getTen().trim().length() == 0) {
            errTen = "Vui lòng nhập tên";
            check++;
        } else if (vc.getTen().length() > 49) {
            errTen = "Tối đa 50 kí tự";
            check++;

        }
        try {
            ptg = Integer.parseInt(vc.getPhanTramGiam());
        } catch (Exception e) {
            errGiaTri = "Vui lòng nhập số";
            check++;
        }

        if (vc.getPhanTramGiam() == null || vc.getPhanTramGiam().trim().length() == 0) {
            errGiaTri = "Vui lòng nhập giá trị";
            check++;

        } else if (ptg == 0) {
            try {
                ptg = Integer.parseInt(vc.getPhanTramGiam());
            } catch (Exception e) {
                errGiaTri = "Vui lòng nhập số";
                check++;
            }
        } else if (ptg <= 0 || ptg > 100) {
            errGiaTri = "Từ 1 - 100";
            check++;

        }
        if (vc.getSoLuong() == null || vc.getSoLuong().trim().length() == 0) {
            errSoLuong = "Vui lòng nhập số lượng";
            check++;
        } else if (sl == 0) {
            try {
                sl = Integer.parseInt(vc.getPhanTramGiam());
            } catch (Exception e) {
                errSoLuong = "Vui lòng nhập số";
                check++;
            }
        } else if (sl <= 0) {
            errSoLuong = "Tối thiểu là 1!";
            check++;
        }

        System.out.println("test ngay bat dau: " + vc.getNgayBatDau());

        try {
            LocalDateTime NBD = LocalDateTime.parse(String.valueOf(vc.getNgayBatDau()), formatter);
            if (vc.getNgayBatDau() == null || vc.getNgayBatDau() == "") {
                errNBD = "Vui lòng chọn ngày";
                check++;
            } else if (NBD.isBefore(timeNow)) {
                errNBD = "Tối thiểu từ hôm nay";
                check++;
            }
        } catch (Exception e) {
            errNBD = "Nhập đúng định dạng ngày";
            check++;
        }

        try {
            LocalDateTime NKT = LocalDateTime.parse(String.valueOf(vc.getNgayKetThuc()), formatter);
            if (vc.getNgayKetThuc() == null || vc.getNgayKetThuc() == "") {
                errNKT = "Vui lòng chọn ngày";
                check++;
            } else if (NKT.isBefore(LocalDateTime.parse(String.valueOf(vc.getNgayBatDau()), formatter)) ||
                    LocalDateTime.parse(String.valueOf(vc.getNgayKetThuc()), formatter).isEqual(LocalDateTime.parse(String.valueOf(vc.getNgayBatDau()), formatter))) {
                errNKT = "Phải lớn hơn ngày bắt đầu";
                check++;
            }
        } catch (Exception e) {
            errNKT = "Nhập đúng định dạng ngày";
            check++;
        }


        model.addAttribute("errTen", errTen);
        model.addAttribute("errGiaTri", errGiaTri);
        model.addAttribute("errNBD", errNBD);
        model.addAttribute("errNKT", errNKT);
        model.addAttribute("errSoLuong", errSoLuong);
        return check == 0;
    }


    public void delete(Voucher vc) {
        voucherRepository.delete(vc);
    }

    public void update(Voucher vc) {
        voucherRepository.save(vc);
    }

    public void jobUpdate(List<Voucher> list) {
//        Ngày bắt đầu ở trước (now)  ngay ket thuc ở sau now -> 1 đang diễn ra
//        Ngày bắt đầu ở sau now             -> 0 sắp diễn ra
//        Ngày kết thúc ở trước now                -> 2 kết thúc
        LocalDateTime now = LocalDateTime.now();
        for (Voucher v : list) {
            if ((v.getNgayBatDau().isBefore(now) || v.getNgayBatDau().isEqual(now))
                    && (v.getNgayKetThuc().isAfter(now) || v.getNgayKetThuc().isEqual(now))) {
                System.out.println("vao 1");
                v.setTrangThai(1);
                voucherRepository.save(v);
            } else if (v.getNgayBatDau().isAfter(now)) {
                System.out.println("vao 2");
                v.setTrangThai(0);
                voucherRepository.save(v);
            } else if (v.getNgayKetThuc().isBefore(now)) {
                System.out.println("vao 3");
                v.setTrangThai(2);
                voucherRepository.save(v);
            }
        }
    }

    public void testTrangThai(Voucher v) {
        //        Ngày bắt đầu ở trước (now)  ngay ket thuc ở sau now -> 1 đang diễn ra
//        Ngày bắt đầu ở sau now             -> 0 sắp diễn ra
//        Ngày kết thúc ở trước now                -> 2 kết thúc
        LocalDateTime now = LocalDateTime.now();
        if ((v.getNgayBatDau().isBefore(now) || v.getNgayBatDau().isEqual(now))
                && (v.getNgayKetThuc().isAfter(now) || v.getNgayKetThuc().isEqual(now))) {
            v.setTrangThai(1);
        } else if (v.getNgayBatDau().isAfter(now)) {
            v.setTrangThai(0);
        } else if (v.getNgayKetThuc().isBefore(now)) {
            v.setTrangThai(2);
        } else v.setTrangThai(0);
    }

    public void saveVoucher(VoucherDTO voucherDTO, List<String> listIDCTG) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherDTO, voucher);
        voucher.setPhanTramGiam(Integer.parseInt(voucherDTO.getPhanTramGiam()));
        voucher.setNgayBatDau(LocalDateTime.parse(String.valueOf(voucherDTO.getNgayBatDau()), formatter));
        voucher.setNgayKetThuc(LocalDateTime.parse(String.valueOf(voucherDTO.getNgayKetThuc()), formatter));
        voucher.setSoLuong(Integer.parseInt(voucherDTO.getSoLuong()));
        List<Voucher> list = voucherRepository.findAll();
        String maTemp = "VC0" + list.size();
        int count = 1;
        while (voucherRepository.findByMa(maTemp).size() != 0) {
            maTemp = "VC0" + (list.size() + count);
            count++;
        }
        voucher.setMa(maTemp);


        this.testTrangThai(voucher);
        if (voucher.getId() != null) {
            voucherRepository.save(voucher);
            this.updateCTG_VC(voucher, listIDCTG);

        } else {
            voucherRepository.save(voucher);
            this.saveCTG_VC(voucher, listIDCTG);
        }
    }

    public void saveCTG_VC(Voucher voucher, List<String> listIDCTG) {
        List<ChiTietGiay> list = chiTietGiayService.findAllById(chiTietGiayService.convertStringListToUUIDList(listIDCTG));
        CTG_KhuyenMai ctg_km = new CTG_KhuyenMai();
        IDVoucher idVoucher = new IDVoucher();
        ctg_km.setTrangThai(0);
        idVoucher.setVoucher(voucher);

        for (ChiTietGiay x : list) {
            idVoucher.setChiTietGiay(x);
            ctg_km.setId(idVoucher);
            chCtg_khuyenMaiService.save(ctg_km);
        }

    }

    public void updateCTG_VC(Voucher voucher, List<String> listIDCTG) {
        List<ChiTietGiay> list = chiTietGiayService.findAllById(chiTietGiayService.convertStringListToUUIDList(listIDCTG));
        CTG_KhuyenMai ctg_km = new CTG_KhuyenMai();
        IDVoucher idVoucher = new IDVoucher();
        ctg_km.setTrangThai(0);
        idVoucher.setVoucher(voucher);
        chCtg_khuyenMaiService.deleteByIdKM(voucher);
        for (ChiTietGiay x : list) {
            idVoucher.setChiTietGiay(x);
            ctg_km.setId(idVoucher);
            chCtg_khuyenMaiService.save(ctg_km);
        }

    }
//
//    public List<String> checkPhanTram(List<UUID> listCTG) {
//        List<String> listMaCTG = new ArrayList<>();
//        for (UUID x : listCTG) {
//            List<String> ma = chCtg_khuyenMaiService.exitsKhuyenMai(x);
//            listMaCTG.add(ma);
//        }
//        return listMaCTG;
//    }

//    Chỉ hiện nút sửa khi trạng thái sắp diễn ra
//    Chỉ hiện nút xóa khi trạng thái là sắp diễn ra hoặc đã hết hạn
//    Thêm sự kiện onclick khi check
}
