package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.repository.DiaChiRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class RestControllerThanhToan {
    @Autowired
    DiaChiRepository diaChiRepository;
    @GetMapping("/thanh-toan-phieu-giam-khong-tai-khoan")
    public List<PhieuGiamGia> PGGKhongTaiKhoan(HttpSession session){
        List<PhieuGiamGia> lst = (List<PhieuGiamGia>) session.getAttribute("lstPGG");
        return lst;
    }
    @GetMapping("/lay-token-tai-khoan-dang-nhap")
    public String tokenTaiKhoanDN(HttpSession session){
        String token= (String) session.getAttribute("tokenTT");

        return token;
    }
    @GetMapping("/dia-chi-nhan-hang/{Id}")
    public Optional<DiaChi> getDiaChiNhanHang(@PathVariable int Id) {
        Optional<DiaChi> dc= diaChiRepository.findById(Id);
        if (dc.isPresent()) {
            return dc;
        }
        return null;


    }
}
