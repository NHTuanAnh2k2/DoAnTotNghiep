package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.PhieuGiamGia;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestControllerThanhToan {
    @GetMapping("/thanh-toan-phieu-giam-khong-tai-khoan")
    public List<PhieuGiamGia> PGGKhongTaiKhoan(HttpSession session){
        List<PhieuGiamGia> lst = (List<PhieuGiamGia>) session.getAttribute("lstPGG");
        return lst;
    }
}
