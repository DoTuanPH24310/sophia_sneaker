package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChiTietGiayService {
    @Autowired
    private ChiTietGiayRepository chiTietGiayRepository;

    public int checkCTG = 0;

    List<ChiTietGiay> findAllByIdGiay(List<String> listId) {
        return chiTietGiayRepository.findAllByIdGiay(convertStringListToUUIDList(listId));
    }

//    List<ChiTietGiay> findAll(){
//        return  chiTietGiayRepository.findAll();
//    }

    public List<ChiTietGiay> findAllByTrangThaiEquals(int trangThai) {
        return chiTietGiayRepository.findAllByTrangThaiEquals(trangThai);
    }

    public List<String> findIdByIdGiay(List<String> listIDGiay) {
        return chiTietGiayRepository.findIdByIdGiay(convertStringListToUUIDList(listIDGiay));
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
            checkCTG = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (checkCTG == 1 && listIDCTG.contains("AllCTG") && findAllByTrangThaiEquals(0).size() >= listIDCTG.size()) {
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
}
