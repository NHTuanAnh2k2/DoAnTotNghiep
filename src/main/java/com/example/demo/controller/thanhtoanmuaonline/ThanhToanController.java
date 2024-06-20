package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.GioHangChiTiet;
import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ThanhToanController {
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @RequestMapping("/view-thanh-toan")
    public String viewthanhtoan(Model model, @ModelAttribute("diachikotaikhoan") DiaChi diachikotaikhoan){
        List<GioHangChiTiet> cartItems = gioHangChiTietRepository.findAll();
        int totalQuantity = 0;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            BigDecimal giatien = sanPhamChiTietRepository.findPriceByProductId(item.getSanphamchitiet().getId());
            totalAmount = totalAmount.add(giatien.multiply(BigDecimal.valueOf(item.getSoluong())));
        }
        List<PhieuGiamGia> lst= phieuGiamGiaImp.findAll();
        List<PhieuGiamGia> lstPGG= new ArrayList<>();
        for(PhieuGiamGia p : lst){
            if(p.getTrangthai()==1 && p.getKieuphieu()==false){
                lstPGG.add(p);
            }
        }
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("lstPGG",lstPGG);
        return "customer/thanhtoan";
    }
    @PostMapping("/customer/thanh-toan-khach-le")
    public String thanhtoankhachle(@ModelAttribute("diachikotaikhoan") DiaChi diachikotaikhoan){

        return "";
    }
}
