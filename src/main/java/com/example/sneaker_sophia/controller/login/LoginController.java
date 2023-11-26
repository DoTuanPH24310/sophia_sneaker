package com.example.sneaker_sophia.controller.login;

import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping("home")
    public String home(Model model) {
//        model.addAttribute("data", taiKhoan);
        return "/login/dang_nhap";
    }

    @GetMapping("404")
    public String error404(){
        return "/website/productwebsite/404";
    }


}
