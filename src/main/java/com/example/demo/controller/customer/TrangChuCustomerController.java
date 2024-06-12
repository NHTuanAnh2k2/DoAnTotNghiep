package com.example.demo.controller.customer;

import com.example.demo.entity.Anh;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.repository.AnhRepository;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.repository.customer.TrangChuRepository;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.math.BigDecimal;
import java.util.*;

@Controller
public class TrangChuCustomerController {
    @Autowired
    TrangChuRepository trangChuRepository;

    @Autowired
    KichCoRepository kichCoRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    SanPhamRepositoty sanPhamRepositoty;

    @Autowired
    SanPhamImp sanPhamImp;

    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;

    @Autowired
    ThuongHieuImp thuongHieuImp;

    @Autowired
    MauSacImp mauSacImp;

    @Autowired
    KichCoImp kichCoImp;

    @Autowired
    DeGiayImp deGiayImp;

    @Autowired
    ChatLieuImp chatLieuImp;

    @Autowired
    AnhImp anhImp;
    @Autowired
    AnhRepository anhRepository;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/customer/trangchu")
    public String hienthiTrangChu(Model model) {
        List<Object[]> topspmoinhattrangchu = trangChuRepository.topspmoinhattrangchu();
        model.addAttribute("topspmoinhattrangchu", topspmoinhattrangchu);
        List<Object[]> topspbanchaynhattrangchu = trangChuRepository.topspbanchaynhat();
        model.addAttribute("topspbanchaynhattrangchu", topspbanchaynhattrangchu);
        return "customer/trangchu";
    }

    @GetMapping("/detailsanphamCustomer/{id}")
    public String detailsanphamCustomer(@PathVariable Integer id, @RequestParam(required = false) String color, @RequestParam(required = false) String size, Model model) {
        SanPham sanPham = trangChuRepository.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("sanphamchitiet", sanPham.getSpct());

        List<String> danhSachAnh = new ArrayList<>();
        for (SanPhamChiTiet spct : sanPham.getSpct()) {
            for (Anh anh : spct.getAnh()) {
                if (color == null || color.equals(spct.getMausac().getTen())) {
                    danhSachAnh.add(anh.getTenanh());
                }
            }
        }
        model.addAttribute("danhSachAnh", danhSachAnh);

        List<String> sizes = new ArrayList<>();
        List<String> colors = new ArrayList<>();
        Set<String> thuongHieuSet = new HashSet<>();
        Set<String> chatlieuset = new HashSet<>();
        Map<String, BigDecimal> giaSanPhamMap = new HashMap<>();

        BigDecimal selectedPrice = null;
        String defaultColor = null;
        String defaultSize = null;

        for (SanPhamChiTiet spct : sanPham.getSpct()) {
            if (!sizes.contains(spct.getKichco().getTen())) {
                sizes.add(spct.getKichco().getTen());
            }
            if (!colors.contains(spct.getMausac().getTen())) {
                colors.add(spct.getMausac().getTen());
            }
            if (spct.getThuonghieu() != null) {
                thuongHieuSet.add(spct.getThuonghieu().getTen());
            }
            if (spct.getChatlieu() != null) {
                chatlieuset.add(spct.getChatlieu().getTen());
            }
            giaSanPhamMap.put(spct.getId().toString(), spct.getGiatien());

            if ((color == null || color.equals(spct.getMausac().getTen())) && (size == null || size.equals(spct.getKichco().getTen()))) {
                selectedPrice = spct.getGiatien();
            }
        }

        if (colors.size() > 0) {
            defaultColor = colors.get(0);
        }
        if (sizes.size() > 0) {
            defaultSize = sizes.get(0);
        }

        model.addAttribute("sizes", sizes);
        model.addAttribute("colors", colors);
        model.addAttribute("thuonghieu", String.join(", ", thuongHieuSet));
        model.addAttribute("chatlieu", String.join(", ", chatlieuset));
        model.addAttribute("giaSanPhamMap", giaSanPhamMap);
        model.addAttribute("selectedPrice", selectedPrice);
        model.addAttribute("defaultColor", defaultColor);
        model.addAttribute("defaultSize", defaultSize);

        List<Object[]> page = trangChuRepository.topspmoinhatdetail();
        model.addAttribute("page", page);
        List<Object[]> page2 = trangChuRepository.topspnoibatdetail();
        model.addAttribute("page2", page2);
        return "customer/product-details";
    }








}