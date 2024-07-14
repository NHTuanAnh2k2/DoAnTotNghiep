package com.example.demo.controller.controlleradvice;

import com.example.demo.entity.NguoiDung;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    KhachHangService khachHangService;

    @ModelAttribute
    public void addAttributes(Model model, HttpSession session) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
        if (userDangnhap != null) {
            model.addAttribute("userDangnhap", userDangnhap);
        }
        if (nd != null) {
            model.addAttribute("userHeader", nd.getHovaten());
        }
    }
}
