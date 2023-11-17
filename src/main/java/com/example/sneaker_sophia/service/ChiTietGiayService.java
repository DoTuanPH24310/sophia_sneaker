package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.DTO_API_CTG;
import com.example.sneaker_sophia.entity.*;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChiTietGiayService {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;


    public static int checkCTG = 0;

    List<ChiTietGiay> findAllByIdGiay(List<String> listId) {
        return chiTietGiayRepository.findAllByIdGiay(convertStringListToUUIDList(listId));
    }


    List<ChiTietGiay> findAllById(List<UUID> listIDG) {
        return chiTietGiayRepository.findAllById(listIDG);
    }


    public List<ChiTietGiay> findAllByTrangThaiEquals(int trangThai) {
        return chiTietGiayRepository.findAllByTrangThaiEquals(trangThai);
    }

    public List<String> findIdByIdGiay(List<String> listIDGiay) {
        return chiTietGiayRepository.findIdByIdGiay(convertStringListToUUIDList(listIDGiay));
    }

    public String findMaByIdCTG(UUID id) {
        return chiTietGiayRepository.findMaByIdCTG(id);
    }


    public List<String> checkedCTG(List<String> listIDCTG, Model model, List<String> listIDG) {

        List<String> temp = listIDCTG;
        // Khi chọn All lần đầu tiên
        if (listIDCTG.contains("AllCTG") && checkCTG == 0) {
            checkCTG = 1;
            model.addAttribute("checkAllCTG", true);
            return this.findIdByIdGiay(listIDG);
        }

        // Khi đã chọn All nhưng lại không chọn nữa
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") == false && listIDCTG.size() <= findAllByTrangThaiEquals(0).size()) {
//            listIDCTG.remove("AllG");
            checkCTG = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() >= listIDCTG.size()) {
//            listIDCTG.remove("AllG");
            checkCTG = 0;
            model.addAttribute("checkAllCTG", false);
            listIDCTG.remove("AllCTG");
            return listIDCTG;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (listIDCTG.size() == findAllByTrangThaiEquals(0).size() && checkCTG == 0) {
            checkCTG = 1;
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }

//        Khi đã chọn all nhưng không bỏ chọn giá trị nào khác(khi gọi lại server)
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() < listIDCTG.size()) {
//            listIDCTG.remove("AllG");
            listIDCTG.remove("AllCTG");
            model.addAttribute("checkAllCTG", true);
            return listIDCTG;
        }
        return temp;
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

    public static final int PRODUCT_DETAIL_PER_PAGE = 10;

    public List<ChiTietGiay> getAll() {
        return chiTietGiayRepository.findAll();
    }

    public void save(ChiTietGiay chiTietGiay) {
        chiTietGiayRepository.save(chiTietGiay);
    }

    public ChiTietGiay getOne(UUID id) {
        return chiTietGiayRepository.findById(id).get();
    }

    public void delete(UUID id) {
        chiTietGiayRepository.deleteById(id);
    }

    public Page<ChiTietGiay> findAll(Pageable pageable) {
        return chiTietGiayRepository.findAll(pageable);
    }

    public Page<ChiTietGiay> listByPageAndProductName(int pageNum, String sortField, String sortDir, String keyword, String productName) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);

        if (StringUtils.isEmpty(productName) && StringUtils.isEmpty(keyword)) {
            return chiTietGiayRepository.findAll(pageable);
        } else if (StringUtils.isEmpty(productName)) {
            return chiTietGiayRepository.findByKeyword(keyword, pageable);
        } else if (StringUtils.isEmpty(keyword)) {
            return chiTietGiayRepository.findByGiay_TenContainingIgnoreCase(productName, pageable);
        } else {
            return chiTietGiayRepository.findByMaAndKeyWord(keyword, productName, pageable);
        }
    }

    public Page<ChiTietGiay> filterCombobox(int pageNum, String sortField, String sortDir, Giay giay, DeGiay deGiay, Hang hang, LoaiGiay loaiGiay, MauSac mauSac, KichCo kichCo, Double giaMin, Double giaMax) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_DETAIL_PER_PAGE, sort);
        return chiTietGiayRepository.findChiTietGiayByMultipleParams(giay, deGiay, hang, loaiGiay, mauSac, kichCo, giaMin, giaMax, pageable);
    }

    //28-10 hoanghh
    public List<ChiTietGiay> findChiTietGiayByMultipleParamsAPI(DTO_API_CTG ctg) {
        System.out.println(ctg.getIdLoaigiay() + "test2");
        UUID idGiay = !ctg.getIdGiay().equals("null") ? UUID.fromString(ctg.getIdGiay()) : null;
        UUID idDeGiay = !ctg.getIdDe().equals("null") ? UUID.fromString(ctg.getIdDe()) : null;
        UUID idHang = !ctg.getIdHang().equals("null") ? UUID.fromString(ctg.getIdHang()) : null;
        UUID idLoaiGiay = !ctg.getIdLoaigiay().equals("null") ? UUID.fromString(ctg.getIdLoaigiay()) : null;
        UUID idMau = !ctg.getIdMau().equals("null") ? UUID.fromString(ctg.getIdMau()) : null;
        UUID idKichCo = !ctg.getIdSize().equals("null") ? UUID.fromString(ctg.getIdSize()) : null;
        String textSearch = ctg.getTextSearch() == null || ctg.getTextSearch().trim().length() == 0 ? null : "%" + ctg.getTextSearch() + "%";
        return chiTietGiayRepository.findChiTietGiayByMultipleParamsAPI(idGiay, idDeGiay, idHang, idLoaiGiay, idMau, idKichCo, textSearch);
    }

    // 28-10 cuongdv
    public ChiTietGiay getChiTietGiayByIdctg(UUID idct) {
        return chiTietGiayRepository.findById(idct).orElse(null);
    }

    // 29-10 cuongdv
    public UUID getIdCTGByMa(String maCTG) {
        return chiTietGiayRepository.getIdCTGByMa(maCTG);
    }

    //    29/10 hoangnh
    public Integer findSoLuongTon(String ma) {
        return chiTietGiayRepository.findSoLuongTon(ma);
    }
    // 11- 11
    public ChiTietGiay getCTGByQrCode(String qrcode){
        return chiTietGiayRepository.getChiTietGiayByQrCode(qrcode);
    }
    // 13-11
    public List<ChiTietGiay> getAllCTG(){
        return chiTietGiayRepository.findAll();
    }

//    14/11
    public Integer findSoLuongTonByQrCode(String qr){
        return chiTietGiayRepository.findSoLuongTonByQrCode(qr);
    }

    // 17/11

    public Integer tongKMByIdctg(UUID idctg){
        return chiTietGiayRepository.tongKMByIdctg(idctg);
    }
}