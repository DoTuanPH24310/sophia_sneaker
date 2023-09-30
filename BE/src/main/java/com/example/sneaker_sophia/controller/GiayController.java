package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.entity.Giay;
import com.example.sneaker_sophia.repository.GiayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GiayController {

    @Autowired
    GiayRepo giayRepo;

    @GetMapping("/giay")
    public String index(Model model) {
        List<Giay> listNv = new ArrayList<>();
        model.addAttribute("listNV", listNv);
        return "giay";
    }
}
