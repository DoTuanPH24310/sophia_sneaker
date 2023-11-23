package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.VoucherDTO;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.VoucherRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ServletRequest request;

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

    public List<Voucher> findByTrangThaiNotLike(){
        return voucherRepository.findByTrangThaiNotLike();
    }


    public Page locVaTimKiem(Pageable pageable, Model model) {
        Page page = null;
        String txtSearchReq = request.getParameter("textSearch");
        String trangThaiReq = request.getParameter("trangThai");
        String txtSearch = txtSearchReq == null || txtSearchReq.isBlank() ? null : "%"+txtSearchReq+"%";

        Integer check = trangThaiReq == null || trangThaiReq.isBlank() ? null : Integer.parseInt(trangThaiReq);
        model.addAttribute("textSearch", txtSearchReq);
        model.addAttribute("trangThai", trangThaiReq);
        if (trangThaiReq != null && trangThaiReq.equals("-1")) {
          return voucherRepository.findAll(pageable);
        }

        page = voucherRepository.locVaTimKiem(check, txtSearch, pageable);
        return page;
    }


    public boolean validate(VoucherDTO vc, Model model, List<String> listIDCTG) {
        int check = 0;
        String errTen = null, errGiaTri = null, errNBD = null, errNKT = null, err = null,errSoLuong = null;

        if (vc.getTen() == null || vc.getTen().trim().length() == 0) {
            errTen = "Vui lòng nhập tên";
            check++;
        }
        if (vc.getPhanTramGiam() == null) {
            errGiaTri = "Nhập giá trị";
            check++;

        } else if (vc.getPhanTramGiam() <= 0 || vc.getPhanTramGiam() > 100) {
            errGiaTri = "Từ 1 - 100";
            check++;
        } else {
            Map<UUID, Integer> map = checkPhanTram(chiTietGiayService.convertStringListToUUIDList(listIDCTG), vc.getPhanTramGiam());
            if (!map.isEmpty()) {
                err = "Tồn tại các khuyến mại trước đó:  \n\n";
                for (Map.Entry<UUID, Integer> entry : map.entrySet()) {
                    err += "Giày: " + chiTietGiayService.findMaByIdCTG(entry.getKey()) + "- Chỉ giảm được tối đa: " + entry.getValue() + "%\n";
                }
                check++;
            }

        }
        if (vc.getSoLuong() == null){
            errSoLuong = "Vui lòng nhập số lượng";

        }else if (vc.getSoLuong() <= 0){
            errSoLuong = "Tối thiểu là 1!";
        }
        LocalDateTime timeNow = LocalDateTime.now().minusMinutes(3);

        if (vc.getNgayBatDau() == null) {
            errNBD = "Vui lòng chọn ngày";
            check++;
        } else if (vc.getNgayBatDau().isBefore(timeNow)) {
            errNBD = "Tối thiểu từ hôm nay" ;
            check++;
        }

        if (vc.getNgayKetThuc() == null) {
            errNKT = "Vui lòng chọn ngày";
            check++;
        } else if (vc.getNgayKetThuc().isBefore(vc.getNgayBatDau()) || vc.getNgayKetThuc().isEqual(vc.getNgayBatDau())) {
            errNKT = "Phải lớn hơn ngày bắt đầu";
            check++;
        }

        model.addAttribute("errTen", errTen);
        model.addAttribute("errGiaTri", errGiaTri);
        model.addAttribute("errNBD", errNBD);
        model.addAttribute("errNKT", errNKT);
        model.addAttribute("err", err);
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
                v.setTrangThai(1);
                voucherRepository.save(v);
            } else if (v.getNgayBatDau().isAfter(now)) {
                v.setTrangThai(0);
                voucherRepository.save(v);
            } else if (v.getNgayKetThuc().isBefore(now)) {
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
        Voucher voucher = new Voucher();
        BeanUtils.copyProperties(voucherDTO, voucher);
        System.out.println(voucher+ "test9");
        voucher.setMa("VC31");
        this.testTrangThai(voucher);
        if (voucher.getId() != null){
            voucherRepository.save(voucher);
            this.updateCTG_VC(voucher, listIDCTG);

        }else{
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

    public Map checkPhanTram(List<UUID> listCTG, Integer phanTram) {
        Map<UUID, Integer> map = new HashMap<>();
        Integer count = 0;
        Integer temp = 0;
        for (UUID x : listCTG) {
            count = chCtg_khuyenMaiService.sumPhanTram(x);
            if (count != null) {
                temp = 100 - (count + phanTram);
                if (temp < 0) {
                    map.put(x, 100 - count);
                }
            }
        }
        return map;
    }

//    Chỉ hiện nút sửa khi trạng thái sắp diễn ra
//    Chỉ hiện nút xóa khi trạng thái là sắp diễn ra hoặc đã hết hạn
//    Thêm sự kiện onclick khi check
}
