package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin/qlhoadon")
    public String qlhoadon() {
        return "admin/qlhoadon";
    }

    @GetMapping("/admin/qlchitiethoadon")
    public String qlchitiethoadon() {
        return "admin/qlchitiethoadon";
    }

    @GetMapping("/admin/qlthongke")
    public String admin() {
        return "admin/qlthongke";
    }

    @GetMapping("/admin/qlkhachhang")
    public String qlkhachhang() {
        return "/admin/qlkhachhang";
    }

    @GetMapping("/admin/addkhachhang")
    public String addkhachhang() {
        return "/admin/addkhachhang";
    }

    @GetMapping("/admin/qlchitietsanpham")
    public String qlchitietsanpham() {
        return "/admin/qlchitietsanpham";
    }

    @GetMapping("/admin/tab")
    public String tab() {
        return "/admin/tab";
    }


}
