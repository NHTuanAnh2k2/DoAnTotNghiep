package com.example.demo.controller;

import com.example.demo.entity.NguoiDung;
import com.example.demo.service.NguoiDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @Autowired
    NguoiDungService nguoiDungService;
    @GetMapping("/same/login")
    public String display(@ModelAttribute("nguoidung") NguoiDung nguoiDung) {
        return "same/login";
    }

//    @PostMapping("/same/login")
//    public String login(
//            @RequestParam("taikhoan") String taikhoan,
//            @RequestParam("matkhau") String matkhau,
//            HttpSession session
//            ) {
//        NguoiDung nguoiDung = nguoiDungService.findByTaiKhoan(taikhoan);
//        if(nguoiDung != null && nguoiDung.getMatkhau().equals(matkhau)) {
//            session.setAttribute("tennguoidung", nguoiDung.getHovaten());
//            System.out.println(nguoiDung.getTaikhoan());
//            return "redirect:/trangchu";
//        } else {
//            return "redirect:/same/login";
//        }
//    }

}
