package com.example.demo.controller;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CustomerController {
    @Autowired
    KhachHangService khachHangService;
    @GetMapping("/trangchu")
    public String trangchu() {
        return "customer/trangchu";
    }

    @GetMapping("/modal")
    public String modal() {
        return "customer/modal";
    }
    @GetMapping("/product/detail")
    public String productdetail() {
        return "customer/product-details";
    }
    @GetMapping("/cart")
    public String cart() {
        return "customer/cart";
    }

    @GetMapping("/quenmatkhau")
    public String quenmatkhau(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                              Model model) {
        return "same/quenmatkhau";
    }

    @GetMapping("/hosokhachhang")
    public String hosokhachhang(Model model,
                                HttpSession session
                                ) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        if (userDangnhap != null) {
            NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
            DiaChi dc = khachHangService.findDiaChiByIdNguoidung(nd.getId());
            model.addAttribute("nguoidung", nd);
            model.addAttribute("diachi", dc);
            model.addAttribute("userDangnhap", userDangnhap);
            return "customer/hosokhachhang";
        } else {
            return "redirect:/dangnhap";
        }

    }


}
