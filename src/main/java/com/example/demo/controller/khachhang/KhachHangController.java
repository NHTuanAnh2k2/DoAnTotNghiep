package com.example.demo.controller.khachhang;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    @GetMapping("/add")
    public String form(@ModelAttribute("khachhang") NguoiDung nguoiDung
                       ) {
        return "admin/addkhachhang";
    }
    @PostMapping("/add")
    public String add(@Valid KhachHang khachHang,
                      @Valid NguoiDung nguoiDung,
                      Model model
                      ) throws IOException {
//        nguoiDung.setAnh(multipartFile.getOriginalFilename());
//        multipartFile.transferTo(new File("D:\\DuAnTotNghiep\\DATN\\src\\main\\resources\\static\\img\\khachhang" + multipartFile.getOriginalFilename()));
//        model.addAttribute("uploadedFile", multipartFile);
        nguoiDungService.save(nguoiDung);
        khachHang.setNguoidung(nguoiDung);
        khachHangService.add(khachHang);
        return "redirect:/khachhang/add";
    }
}
