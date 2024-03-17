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

    @GetMapping("/admin/qlsanpham")
    public String table() {
        return "admin/qlsanpham";
    }

    @GetMapping("/admin/addsanpham")
    public String viewAdd() {
        return "admin/addsanpham";
    }

    @GetMapping("/admin/qlnhanvien")
    public String qlnhanvien() {
        return "/admin/qlnhanvien";
    }

    @GetMapping("/admin/addnhanvien")
    public String addnhanvien() {
        return "/admin/addnhanvien";
    }

    @GetMapping("/admin/qlkhachhang")
    public String qlkhachhang() {
        return "/admin/qlkhachhang";
    }

    @GetMapping("/admin/addkhachhang")
    public String addkhachhang() {
        return "/admin/addkhachhang";
    }

    @GetMapping("/admin/qltheloai")
    public String qltheloai() {
        return "/admin/qltheloai";
    }

    @GetMapping("/admin/addtheloai")
    public String addtheloai() {
        return "/admin/addtheloai";
    }

    @GetMapping("/admin/qlchitietsanpham")
    public String qlchitietsanpham() {
        return "/admin/qlchitietsanpham";
    }


    @GetMapping("/layout")
    public String header() {
        return "/layout/header";
    }

    @GetMapping("/admin/addthuonghieu")
    public String thuonghieu() {
        return "/admin/addthuonghieu";
    }

    @GetMapping("/admin/tab")
    public String tab() {
        return "/admin/tab";
    }

    @GetMapping("/admin/qldegiay")
    public String qldegiay() {
        return "/admin/qldegiay";
    }

    @GetMapping("/admin/qlchatlieu")
    public String qlchatlieu() {
        return "/admin/qlchatlieu";
    }

    @GetMapping("/admin/qlthuonghieu")
    public String qlthuonghieu() {
        return "/admin/qlthuonghieu";
    }


}
