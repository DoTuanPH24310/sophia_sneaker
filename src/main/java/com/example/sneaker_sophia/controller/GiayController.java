package com.example.sneaker_sophia.controller;

import com.example.sneaker_sophia.repository.GiayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GiayController {

    @Autowired
    GiayRepo giayRepo;

    @GetMapping("/giay")
    public String index(Model model) {
        return "admin/index";
    }
}
