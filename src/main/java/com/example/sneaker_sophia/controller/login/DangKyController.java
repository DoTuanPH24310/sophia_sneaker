package com.example.sneaker_sophia.controller.login;

import com.example.sneaker_sophia.repository.VaiTroRepository;
import com.example.sneaker_sophia.request.TaiKhoanRequest;
import com.example.sneaker_sophia.service.EmailService;
import com.example.sneaker_sophia.service.TaiKhoanService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/register")
public class DangKyController {
    public static int check = 0;
    public static StringBuilder otpT;
    @Resource(name = "taiKhoanService")
    TaiKhoanService taiKhoanService;

    @Resource(name = "vaiTroRepository")
    VaiTroRepository vaiTroRepository;

    @Autowired
    EmailService emailService;

    @GetMapping("/form")
    public String formDK(Model model) {
        TaiKhoanRequest khachHang = new TaiKhoanRequest();
        model.addAttribute("khachHang", khachHang);
        check = 0;
        return "/login/dangky";
    }

    @PostMapping("/add")
    public String addKH(
            Model model,
            @ModelAttribute(value = "khachHang") TaiKhoanRequest kh_rq,
            @RequestParam(value = "otp", required = false) String otp
    ) {


        kh_rq.setIdVaiTro(vaiTroRepository.getIdByTenKH());
        if (!taiKhoanService.validateAddTK(kh_rq, model)) {
            return "/login/dangky";
        } else {
            if (check == 0) {
                String numbers = "0123456789";
                Random random = new Random();
                otpT = new StringBuilder(6);

                for (int i = 0; i < 6; i++) {
                    int index = random.nextInt(numbers.length());
                    otpT.append(numbers.charAt(index));
                }
                emailService.guiEmailOTP(kh_rq.getEmail(), otpT.toString());
                model.addAttribute("showEmailVerification", true);
                check = 1;
            } else {
                if (kh_rq.getOtp() != null) {

                    try {
                        Integer.parseInt(otp);
                    } catch (NumberFormatException e) {
                        model.addAttribute("errOTP", "Không nhập đúng OTP");
                        model.addAttribute("showEmailVerification", true);
                        check = 1;
                        return "/login/dangky";
                    }

                    if (kh_rq.getOtp().equals("") || kh_rq.getOtp() == null || !otp.equals(otpT.toString())) {
                        model.addAttribute("errOTP", "Không nhập đúng OTP");
                        model.addAttribute("showEmailVerification", true);
                        check = 1;
                        return "/login/dangky";
                    } else {
                        taiKhoanService.addkh(kh_rq);
                        check = 0;
                        return "redirect:/login/home";
                    }
                }
            }

            return "/login/dangky";
        }

    }

}
