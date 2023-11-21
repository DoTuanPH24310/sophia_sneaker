package com.example.sneaker_sophia.service;

import com.example.sneaker_sophia.dto.GiayRequest;
import com.example.sneaker_sophia.entity.ChiTietGiay;
import com.example.sneaker_sophia.entity.DeGiay;
import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.entity.Hang;
import com.example.sneaker_sophia.repository.GiayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GiayService {
    @Autowired
    private GiayRepository giayRepository;

    @Autowired
    private ChiTietGiayService chiTietGiayService;
    public static int check = 0;
    public List<ChiTietGiay> listCTG = new ArrayList<>();


    public List<Giay> getAll(){
        return giayRepository.findAll();
    }

    public boolean checkMa(String ma) {
        for (Giay giay : this.giayRepository.findAll()) {
            if (giay.getMa().equals(ma)) {
                return false;
            }
        }
        return true;
    }

    public Giay add(GiayRequest giayRequest) {
        Giay giay = giayRequest.loadForm(new Giay());
        if (checkMa(giay.getMa())) {
            return this.giayRepository.save(giay);
        }
        return null;
    }

    public Optional<Giay> findOne(UUID id) {
        return this.giayRepository.findById(id);
    }

    public Page<Giay> fillter(String txtSearch, String trangThai, Pageable pageable) {
        Page<Giay> page = null;

        if (txtSearch == null || txtSearch.trim().isEmpty()) {
            if (trangThai == null || trangThai == "" || trangThai.equals("-1")) {
                return giayRepository.getAll(pageable);
            }

            Giay giay = Giay.builder()
                    .trangThai(Integer.parseInt(trangThai))
                    .build();

            page = giayRepository.findAll(Example.of(giay), pageable);
        } else {
            if (trangThai == null || trangThai.equals("-1")) {
                page = giayRepository.searchWithoutTrangThai(txtSearch, pageable);
            } else {
                page = giayRepository.searchAndFilter(txtSearch, trangThai, pageable);
            }
        }

        return page;
    }

    public Giay update(UUID id, GiayRequest giayRequest) {
        Optional<Giay> optional = this.giayRepository.findById(id);
        return optional.map(o -> {
            o.setId(giayRequest.getId());
            o.setMa(giayRequest.getMa());
            o.setTen(giayRequest.getTen());
            o.setTrangThai(giayRequest.getTrangThai());
            return this.giayRepository.save(o);
        }).orElse(null);
    }

    public Giay delete(UUID id) {
        Optional<Giay> optional = this.giayRepository.findById(id);
        return optional.map(o -> {
            this.giayRepository.delete(o);
            return o;
        }).orElse(null);
    }


    public List<Giay> findAllByTrangThaiEquals(int trangThai) {
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
            listCTG = chiTietGiayService.findAllByIdGiay(this.findAllID(0));
            model.addAttribute("listCTG", listCTG);
            return this.findAllID(0);
        }

        // Khi đã chọn All nhưng lại không chọn nữa
        if (check == 1 && !listId.contains("AllG")) {
            check = 0;
            return new ArrayList<String>();
        }

//        Khi đã chọn All sau đó lại bỏ  chọn 1 trong  các giá trị bên dưới
        if (check == 1 && listId.contains("AllG") && findAllByTrangThaiEquals(0).size() >= listId.size()) {
            check = 0;
            model.addAttribute("checkAll", false);
            listId.remove("AllG");
            listCTG = chiTietGiayService.findAllByIdGiay(listId);
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

//          Khi đã chọn all nhưng không bỏ chọn giá trị nào khác(khi gọi lại server, khi chọn CTG)
//        Bởi vì khi đã chọn giày nhưng lại chọn chi tiết thì giá trị check vẫn là 1
        if (check == 1 && listId.contains("AllG") && findAllByTrangThaiEquals(0).size() < listId.size()) {
            listId.remove("AllG");
            model.addAttribute("checkAll", true);
            listCTG = chiTietGiayService.findAllByIdGiay(listId);
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

//      Khi số lượng sản phẩm được chọn bằng với số lượng sản phẩm trong kho
        if (listId.size() == findAllByTrangThaiEquals(0).size() && check == 0) {
            check = 1;
            model.addAttribute("checkAll", true);
            listCTG = chiTietGiayService.findAllByIdGiay(this.findAllID(0));
            model.addAttribute("listCTG", listCTG);
            return listId;
        }

        model.addAttribute("listCTG", chiTietGiayService.findAllByIdGiay(temp));
        return temp;
    }

    public Giay getOne(UUID uuid) {
        return giayRepository.findById(uuid).get();
    }

    public Giay findByTen(String ten){
        return giayRepository.findGiayByTen(ten);
    }

}
