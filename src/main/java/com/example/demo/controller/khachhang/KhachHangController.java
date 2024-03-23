package com.example.demo.controller.khachhang;

import com.example.demo.entity.KhachHang;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("khachhang")
public class KhachHangController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;

    @GetMapping
    public String display(Model model, @ModelAttribute("khachhang") KhachHang khachHang) {
        model.addAttribute("lstKhachHang", khachHangService.findAll());
        model.addAttribute("lstNguoiDung", nguoiDungService.findAll());
        return "admin/qlkhachhang";
    }
//    @PostMapping("/timkiem")
//    public String search(@RequestParam(required = false) String tenOrSdt,
//                         @RequestParam(required = false) int trangThai,
//                         @RequestParam(required = false) Date ngaySinh
//                         ){
//        List<KhachHang> lstTimKiem = null;
//        if (tenOrSdt != null) {
//            lstTimKiem = khachHangService.findByTenOrSdt(tenOrSdt);
//        } else if (trangThai != null) {
//
//        }
//    }
}
