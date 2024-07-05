package com.example.demo.controller.controlleradvice;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        if (userDangnhap != null) {
            model.addAttribute("userDangnhap", userDangnhap);
        }
    }
}
