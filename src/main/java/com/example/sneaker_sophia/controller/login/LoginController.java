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
    private TaiKhoan taiKhoan;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping("home")
    public String home(Model model) {
//        model.addAttribute("data", taiKhoan);
        return "/login/dang_nhap.html";
    }

//    @PostMapping("home")
//    public String login(@ModelAttribute("data") TaiKhoan taiKhoan, Model model) {
//        // Xử lý đăng nhập bằng thông tin từ taiKhoan (email và matKhau)
//        boolean authenticationResult = userDetailsService.authenticateUser(taiKhoan.getEmail(), taiKhoan.getMatKhau());
//
//        if (authenticationResult) {
//            // Đăng nhập thành công
//            return "redirect:/admin/giay/hien-thi";
//        } else {
//            // Đăng nhập không thành công
//            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu không đúng");
//            return "login/dang_nhap.html";
//        }
//    }




}
