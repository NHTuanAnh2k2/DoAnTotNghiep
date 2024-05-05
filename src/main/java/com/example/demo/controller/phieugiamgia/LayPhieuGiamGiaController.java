package com.example.demo.controller.phieugiamgia;

import com.example.demo.entity.DotGiamGia;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.PhieuGiamGia;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LayPhieuGiamGiaController {
    @GetMapping("/lay-phieu-giam-gia")
    public PhieuGiamGia layphieu(HttpSession session){
        PhieuGiamGia phieuGiamGia = (PhieuGiamGia) session.getAttribute("phieuGG");
        return phieuGiamGia;
    }
    @GetMapping("/lay-khach-hang")
    public List<KhachHang> layKhachHang(HttpSession session){
        List<KhachHang> lstKH = (List<KhachHang>) session.getAttribute("lstKH");
        return lstKH;
    }
    @GetMapping("/lay-khach-hang-phieu-giam")
    public List<KhachHangPhieuGiam> layKhachHangPhieuGiam(HttpSession session){
        List<KhachHangPhieuGiam> lstKHPG = (List<KhachHangPhieuGiam>) session.getAttribute("lstKHPG");
        return lstKHPG;
    }
}
