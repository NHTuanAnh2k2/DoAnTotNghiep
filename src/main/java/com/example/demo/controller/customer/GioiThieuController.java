package com.example.demo.controller.customer;

import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


@Controller
public class GioiThieuController {

    @Autowired
    HttpSession session;

    @Autowired
    NguoiDungGioHangRepository nguoiDungGioHangRepository;

    @Autowired
    KhachHangGioHangRepository khachHangGioHangRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @GetMapping("/customer/gioithieu")
    public String gioithieu(Model model) {
        List<GioHangChiTiet> cartItems = new ArrayList<>();
        int totalQuantity = 0;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
        }
        model.addAttribute("totalQuantity", totalQuantity);
        return "customer/gioithieu";
    }
}
