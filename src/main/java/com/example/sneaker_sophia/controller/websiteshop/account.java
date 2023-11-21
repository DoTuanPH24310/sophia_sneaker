package com.example.sneaker_sophia.controller.websiteshop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("my-account")
public class account {

    @GetMapping("home")
    public String home(){

        return "website/productwebsite/my-account";
    }
}
