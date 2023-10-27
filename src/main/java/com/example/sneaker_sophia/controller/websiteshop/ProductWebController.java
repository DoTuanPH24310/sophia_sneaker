package com.example.sneaker_sophia.controller.websiteshop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("sophia-store")
public class ProductWebController {

    @GetMapping("/product")
    public String home(){
        return "website/product-details-default";
    }
}
