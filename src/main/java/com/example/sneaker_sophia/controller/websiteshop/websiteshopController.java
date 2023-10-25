package com.example.sneaker_sophia.controller.websiteshop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class websiteshopController {
    @GetMapping("/sophia-store/home")
    public String home(){
        return "website/index-2";
    }
}
