package com.example.sneaker_sophia.controller.websiteshop;

import com.example.sneaker_sophia.dto.TaiKhoanDTO;
import com.example.sneaker_sophia.entity.DiaChi;
import com.example.sneaker_sophia.entity.TaiKhoan;
import com.example.sneaker_sophia.repository.AccountRepository;
import com.example.sneaker_sophia.repository.LoginRepository;
import com.example.sneaker_sophia.service.AccountService;
import com.example.sneaker_sophia.service.DiaChiCheckoutService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("my-account")
public class account {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DiaChiCheckoutService diaChiService;

    @GetMapping("home")
    public String home(Model model, HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {

            DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();
            TaiKhoan taiKhoan = this.loginRepository.findByEmail(authentication.getName());
            List<DiaChi> diaChiList = accountRepository.findByTaiKhoan_Email(authentication.getName());
            if (diaChi != null) {

                session.setAttribute("tinh", diaChi.getTinh());
                session.setAttribute("quan", diaChi.getQuanHuyen());
                session.setAttribute("phuong", diaChi.getPhuongXa());
            } else {
                diaChi = new DiaChi();
            }
            model.addAttribute("account", taiKhoan);
            model.addAttribute("diaChii", diaChi);
            model.addAttribute("diaChiList", diaChiList);
            return "website/productwebsite/my-account";
        }
        return "redirect:/login/home";
    }

    @PostMapping("update-account")
    public String updateAccount(@Valid @ModelAttribute("account") TaiKhoanDTO taiKhoanDTO, BindingResult result,
                                HttpSession session, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
            if (result.hasErrors()) {
                DiaChi diaChi = diaChiService.getDiaChiOfLoggedInUser();
                List<DiaChi> diaChiList = accountRepository.findByTaiKhoan_Email(authentication.getName());
                if (diaChi != null) {

                    session.setAttribute("tinh", diaChi.getTinh());
                    session.setAttribute("quan", diaChi.getQuanHuyen());
                    session.setAttribute("phuong", diaChi.getPhuongXa());
                } else {
                    diaChi = new DiaChi();
                }
                model.addAttribute("diaChii", diaChi);
                model.addAttribute("diaChiList", diaChiList);
                return "website/productwebsite/my-account";
            }

            this.accountService.updateTaiKhoan(taiKhoanDTO);

            return "redirect:/my-account/home";
        }
        return "redirect:/login/home";
    }

}
