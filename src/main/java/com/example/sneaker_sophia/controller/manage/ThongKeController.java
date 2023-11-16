package com.example.sneaker_sophia.controller.manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin/")
public class ThongKeController {
    @GetMapping("thong-ke-theo-thang")
    public String index() {
        return "admin/thongKe/thongKe";
    }
}
