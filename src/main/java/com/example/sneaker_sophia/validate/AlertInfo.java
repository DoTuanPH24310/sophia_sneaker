package com.example.sneaker_sophia.validate;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AlertInfo {
    @Autowired
    HttpSession session;
    public void alert(String title, String info){
        if (info == null) info = "Thao tác không chính xác";
        session.setAttribute(title,info);
    }
}
