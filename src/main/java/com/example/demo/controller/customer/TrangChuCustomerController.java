package com.example.demo.controller.customer;

import com.example.demo.entity.Anh;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.AnhRepository;
import com.example.demo.repository.KichCoRepository;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.service.impl.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
////
@Controller
public class TrangChuCustomerController {
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
        List<Object[]> page = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc2();
        model.addAttribute("page", page);
        return "customer/trangchu";
    }

    @GetMapping("/detailsanphamCustomer/{id}")
    public String detailsanphamCustomer(@PathVariable Integer id, Model model) {
        // Lấy sản phẩm theo id
        SanPham sanPham = sanPhamRepositoty.findById(id).orElse(null);
        model.addAttribute("sanpham", sanPham);
        model.addAttribute("sanphamchitiet", sanPham.getSpct());

        // Lấy danh sách ảnh
        List<SanPhamChiTiet> sanPhamChiTietList = sanPham.getSpct();
        List<String> danhSachAnh = new ArrayList<>();
        for (SanPhamChiTiet spct : sanPhamChiTietList) {
            for (Anh anh : spct.getAnh()) {
                danhSachAnh.add(anh.getTenanh());
            }
        }
        model.addAttribute("anh", danhSachAnh);

        // Lấy danh sách sản phẩm theo điều kiện
        List<Object[]> page = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc3();
        model.addAttribute("page", page);
        List<Object[]> page2 = sanPhamRepositoty.findProductsWithTotalQuantityOrderByDateDesc4();
        model.addAttribute("page2", page2);

        return "customer/product-details";
    }


//    @GetMapping("/detailsanphamCustomer/{id}")
//    public String detailsanphamCustomer(@PathVariable Integer id, Model model) {
//        // Truy vấn sản phẩm chính
//        SanPham sanPham = sanPhamRepositoty.findById(id).orElse(null);
//
//        // Truy vấn danh sách chi tiết sản phẩm
//        List<SanPhamChiTiet> sanPhamChiTietList = sanPhamChiTietRepository.findBySanPham(sanPham);
//
//        model.addAttribute("sanpham", sanPham);
//        model.addAttribute("sanphamchitiet", sanPhamChiTietList);
//
//        return "customer/product-details";
//    }

}
