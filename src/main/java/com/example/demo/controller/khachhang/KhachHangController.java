package com.example.demo.controller.khachhang;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.restcontroller.khachhang.KhachHangRestController;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.eclipse.tags.shaded.org.apache.xpath.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Controller
@RequestMapping("khachhang")
public class KhachHangController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    KhachHangRestController khachHangRestController;

    @GetMapping
    public String display(Model model, @ModelAttribute("khachhang") KhachHang khachHang,
                          Optional<Integer> p
    ) {
        Pageable pageable = PageRequest.of(p.orElse(0), 10);
        Page<KhachHang> lstKhachHang = khachHangService.findAllKhachHang(pageable);
        model.addAttribute("lstKhachHang", lstKhachHang);
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
    @GetMapping("/add")
    public String form(@ModelAttribute("nguoidung") NguoiDung nguoiDung,
                       @ModelAttribute("diachi") DiaChi diaChi,
                       String idThanhPho,
                       Model model
    ) {
        List<String> cities = khachHangService.getCities();
        List<String> districts = khachHangService.getDistricts(idThanhPho);
        model.addAttribute("cities", cities);
        model.addAttribute("districts", districts);
        return "admin/addkhachhang";
    }

    @PostMapping("/add")
    public String add(@Valid KhachHang khachHang,
                      @Valid NguoiDung nguoiDung,
                      @Valid DiaChi diaChi,
                      @RequestParam("tinhthanhpho") String tinhthanhpho,
                      BindingResult bindingResult,
                      Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/khachhang/add";
        }
        khachHangService.add(khachHang, nguoiDung, diaChi, tinhthanhpho);
        return "redirect:/khachhang/add";
    }


//    @GetMapping("/timkiem")
//    public String timKiem(Model model,
//                          @RequestParam(required = false) String tenHoacSdt,
//                          @RequestParam(required = false) String sdt,
//                          @RequestParam(required = false) int trangthai,
//                          @RequestParam(required = false) Date ngaysinh
//    ) {
//        List<KhachHang> lstTimKiem = khachHangService.findByAll(tenHoacSdt, sdt, trangthai, ngaysinh);
//        model.addAttribute("lstKhachHang", lstTimKiem);
////        model.addAttribute("lstNguoiDung", khachHangService.findAll());
//        return "admin/qlkhachhang";
//    }

    @GetMapping("/capnhat/{id}")
    public String hienThiCapNhat(Model model,
                                 @PathVariable("id") int id
    ) {
        KhachHang khachHang = khachHangService.getOne(id);
        model.addAttribute("getone", khachHang);
        return "admin/updatekhachhang";
    }

    @GetMapping("/qr")
    public String qrcode() {
        return "admin/qrcode";
    }
}
