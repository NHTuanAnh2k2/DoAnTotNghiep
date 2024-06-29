package com.example.demo.controller.thanhtoanmuaonline;

import com.example.demo.entity.*;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.giohang.GioHangChiTietRepository;
import com.example.demo.repository.giohang.GioHangRepository;
import com.example.demo.repository.giohang.KhachHangGioHangRepository;
import com.example.demo.repository.giohang.NguoiDungGioHangRepository;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import jakarta.servlet.http.HttpSession;
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
    KhachHangGioHangRepository khachHangGioHangRepository;
    @Autowired
    NguoiDungGioHangRepository nguoiDungGioHangRepository;

    @Autowired
    private GioHangRepository gioHangRepository;
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    HttpSession session;

    @Autowired
    private GioHangChiTietRepository gioHangChiTietRepository;
    @RequestMapping("/view-thanh-toan")
    public String viewthanhtoan(Model model, @ModelAttribute("diachikotaikhoan") DiaChi diachikotaikhoan, HttpSession session){
        List<GioHangChiTiet> cartItems = new ArrayList<>();
        String token = (String) session.getAttribute("token");
        NguoiDung nguoiDung = null;
        KhachHang khachHang = null;
        GioHang gioHang = null;
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (token != null) {
            // Lấy danh sách token từ session
            List<TaiKhoanTokenInfo> taiKhoanTokenInfos = (List<TaiKhoanTokenInfo>) session.getAttribute("taiKhoanTokenInfos");
            if (taiKhoanTokenInfos != null) {
                // Tìm người dùng từ danh sách token
                for (TaiKhoanTokenInfo tkInfo : taiKhoanTokenInfos) {
                    if (tkInfo.getToken().equals(token)) {
                        Integer userId = tkInfo.getId();
                        nguoiDung = nguoiDungGioHangRepository.findById(userId).orElse(null);
                        break;
                    }
                }
                if (nguoiDung != null) {
                    // Lấy giỏ hàng của người dùng đã đăng nhập
                    khachHang = khachHangGioHangRepository.findByNguoidung(nguoiDung);
                    if (khachHang != null) {
                        gioHang = gioHangRepository.findByKhachhang(khachHang);
                        if (gioHang != null) {
                            cartItems = gioHang.getGioHangChiTietList();
                        }
                    }
                }
            }
        } else {
            // Giỏ hàng của người dùng chưa đăng nhập
            cartItems = (List<GioHangChiTiet>) session.getAttribute("cartItems");
            if (cartItems == null) {
                cartItems = new ArrayList<>();
            }
        }
        // Tính tổng số lượng sản phẩm và tổng tiền
        int totalQuantity = 0;
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GioHangChiTiet item : cartItems) {
            totalQuantity += item.getSoluong();
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
        model.addAttribute("token", token);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("totalQuantity", totalQuantity);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("lstPGG",lstPGG);
        session.setAttribute("lstPGG",lstPGG);
        return "customer/thanhtoan";
    }
    @PostMapping("/customer/thanh-toan-khach-le")
    public String thanhtoankhachle(@ModelAttribute("diachikotaikhoan") DiaChi diachikotaikhoan){

        return "";
    }
}
