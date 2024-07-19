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
        String adminDangnhap = (String) session.getAttribute("adminDangnhap");
        NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
        if (userDangnhap != null) {
            model.addAttribute("userDangnhap", userDangnhap);
        }
        if (adminDangnhap != null) {
            model.addAttribute("adminDangnhap", adminDangnhap);
        }
        if (nd != null) {
            String fullName = nd.getHovaten();
            if (fullName.length() > 11) {
                // Tách tên thành các phần riêng biệt
                String[] parts = fullName.split("\\s+");
                // Lấy phần tên cuối cùng
                String lastName = parts[parts.length - 1];
                //nếu tên mà > 11 thì cắt lấy 11 kí tự
                if (lastName.length() > 11) {
                    lastName = lastName.substring(0, 11);
                }
                // Kiểm tra nếu phần tên đệm cộng với phần tên cuối cùng nhỏ hơn 11 ký tự
                if (parts.length > 1) {
                    String middleName = parts[parts.length - 2];
                    if ((middleName + " " + lastName).length() <= 11) {
                        fullName = middleName + " " + lastName;
                    } else {
                        fullName = lastName;
                    }
                }
            }
            model.addAttribute("userHeader", fullName);
            model.addAttribute("hovatenHoSo", fullName);
        }
    }

}
