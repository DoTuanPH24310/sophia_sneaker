package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.Voucher;
import com.example.sneaker_sophia.repository.VoucherRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;

    public static List<UUID> listUUID = new ArrayList<>();

    @Autowired
    private ServletRequest request;

    @Autowired
    private HttpSession session;

    @Autowired
    private GiayService giayService;

    public Page<Voucher> findAll(Pageable pageable){
        return  voucherRepository.findAll(pageable);
    }

    public Page<Voucher> findAll(Pageable pageable, Example<Voucher> e){
        return  voucherRepository.findAll(e,pageable);
    }

    public Page<Voucher> searchAndFilter(String text, String trangThai, Pageable pageable){
        return voucherRepository.searchAndFilter(text,trangThai,pageable);
    }

    public void addAttributeModel(Model model, List<String> listId, List<String> listIdCTG){
        model.addAttribute("listId", listId);
        model.addAttribute("listIDCTG", listIdCTG);
        model.addAttribute("listGiay", giayService.findAllByTrangThaiEquals(0));
    }


    public Page locVaTimKiem(Pageable pageable, Model model) {
        Page page = null;
        String txtSearch = request.getParameter("textSearch");
        String trangThaiReq = request.getParameter("trangThai");
        String trangThai = trangThaiReq == null || (trangThaiReq.equals("0") == false && trangThaiReq.equals("1") == false) ? "-1" : trangThaiReq;
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("textSearch", txtSearch);

        if (txtSearch == null || txtSearch.trim().length() == 0) {
            if (trangThai.equals("-1")) {
                return voucherRepository.findAll(pageable);
            }
            Voucher voucher = Voucher.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();
            page = voucherRepository.findAll(Example.of(voucher),pageable);
        } else {
            if (trangThai.equals("-1")) {
                return voucherRepository.searchAndFilter(txtSearch, null, pageable);
            }
            page = voucherRepository.searchAndFilter(txtSearch, trangThai, pageable);
        }
        return page;
    }


    public static List<UUID> convertStringListToUUIDList(List<String> stringList) {
        List<UUID> uuidList = new ArrayList<>();

        for (String str : stringList) {
            try {
                UUID uuid = UUID.fromString(str);
                uuidList.add(uuid);
            } catch (IllegalArgumentException e) {
                // Xử lý ngoại lệ nếu chuỗi không hợp lệ
                // Ví dụ: có thể bỏ qua hoặc log thông báo lỗi.
            }
        }

        return uuidList;
    }



}
