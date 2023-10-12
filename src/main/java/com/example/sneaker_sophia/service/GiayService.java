package com.example.sneaker_sophia.service;


import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.ChiTietGiayRepository;
import com.example.sneaker_sophia.repository.GiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GiayService {
    @Autowired
    private GiayRepository giayRepository;

    @Autowired
    private ChiTietGiayService chiTietGiayService;

    public int check = 0;



    public List<ChiTietGiay> listCTG = new ArrayList<>();

    public List<Giay> findAll() {
        return giayRepository.findAll();
    }

    public List<Giay> findAllByTrangThaiEquals(int trangThai){
        return giayRepository.findAllByTrangThaiEquals(trangThai);
    }

    public List<String> findAllID(Integer trangThai) {
        return giayRepository.finAllId(trangThai);
    }

    public List<String> checkedGiay(List<String> listId, Model model) {
        List<String> temp = listId;
        // Khi chọn All lần đầu tiên
        if (listId.contains("AllG") && check == 0) {
            check = 1;
            listId.remove("AllG");
            model.addAttribute("checkAll", true);
            listCTG =chiTietGiayService.findAllByIdGiay(this.findAllID(0));
            model.addAttribute("listCTG", listCTG);
            return this.findAllID(0);
        }

        // Khi đã chọn All nhưng lại không chọn nữa
        if (check == 1 && listId.contains("AllG") == false && listId.size() <= findAllByTrangThaiEquals(0).size()) {
            check = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (check == 1 && listId.contains("AllG") && findAllByTrangThaiEquals(0).size()>= listId.size()) {
            check = 0;
            model.addAttribute("checkAll", false);
            listId.remove("AllG");
            listCTG = chiTietGiayService.findAllByIdGiay(listId);
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (listId.size() == findAllByTrangThaiEquals(0).size() && check == 0) {
            check = 1;
            model.addAttribute("checkAll", true);
            listCTG =  chiTietGiayService.findAllByIdGiay(this.findAllID(0));
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

//        Khi đã chọn all nhưng không bỏ chọn giá trị nào khác(khi gọi lại server)
        if (check == 1 && listId.contains("AllG") && findAllByTrangThaiEquals(0).size() < listId.size()) {
            listId.remove("AllG");
            model.addAttribute("checkAll", true);
            listCTG = chiTietGiayService.findAllByIdGiay(listId);
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

        model.addAttribute("listCTG", chiTietGiayService.findAllByIdGiay(temp));
        return temp;
    }









}
