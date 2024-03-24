package com.example.demo.controller.sanphamchitiet;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
///
@Controller
public class SanPhamChiTietController {
    @Autowired
    SanPhamChiTietImp sanPhamChiTietImp;
    @Autowired
    SanPhamImp sanPhamImp;
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

//    @GetMapping("/listspct")
//    public String hienthi(Model model, @RequestParam(defaultValue = "0") int p) {
//        Pageable pageable= PageRequest.of(p,5);
//        Page<SanPhamChiTiet> page=sanPhamChiTietImp.finAllPage(pageable);
//        model.addAttribute("page",page);
//        return "admin/addsanpham";
//    }


}
