package com.example.sneaker_sophia.service;


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

    public List<Giay> findAll() {
        return giayRepository.findAll();
    }

    public List<String> findAllID() {
        return giayRepository.finAllId();
    }

    public List<String> checkedGiay(List<String> listId, Model model) {
        List<String> temp = listId;
        // Khi chọn All lần đầu tiên
        if (listId.contains("AllG") && check == 0) {
            check = 1;
            model.addAttribute("checkAll", true);
            model.addAttribute("listCTG", chiTietGiayService.findAllByIdGiay(this.findAllID()));
            return this.findAllID();
        }

        // Khi đã chọn All nhưng lại không chọn nữa
        if (check == 1 && listId.contains("AllG") == false && listId.size() <= findAll().size()) {
            check = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All nhưng lại bỏ chọn các giá trị bên dưới
        if (check == 1 && listId.contains("AllG") && listId.size() - 1 < findAll().size()) {
            check = 0;
            model.addAttribute("checkAll", false);
            listId.remove("AllG");
            model.addAttribute("listCTG", chiTietGiayService.findAllByIdGiay(listId));
            return listId;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (listId.size() == findAll().size() && check == 0) {
            model.addAttribute("checkAll", true);
            model.addAttribute("listCTG",  chiTietGiayService.findAllByIdGiay(this.findAllID()));
            return listId;
        }


        model.addAttribute("listCTG", chiTietGiayService.findAllByIdGiay(temp));
        return temp;
    }


}
