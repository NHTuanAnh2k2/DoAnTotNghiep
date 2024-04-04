package com.example.demo.controller.khachhang;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public String form(@ModelAttribute("khachhang") NguoiDung nguoiDung) {
        return "admin/addkhachhang";
    }
    @PostMapping("/add")
    public String add(@Valid KhachHang khachHang,
                      @Valid NguoiDung nguoiDung,
                      @RequestParam("file") MultipartFile multipartFile,
                      BindingResult bindingResult,
                      Model model
                      ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/khachhang/add";
        }
        nguoiDung.setAnh(multipartFile.getOriginalFilename());
        multipartFile.transferTo(new File("D:\\DuAnTotNghiep\\DATN\\src\\main\\resources\\static\\img\\khachhang" + multipartFile.getOriginalFilename()));
        nguoiDungService.save(nguoiDung);
        khachHang.setNguoidung(nguoiDung);
        khachHang.setTrangthai(nguoiDung.getTrangthai());
        khachHangService.add(khachHang);
        return "redirect:/khachhang/add";
    }
    @GetMapping("/timkiem")
    public String timKiem(Model model,
                          @RequestParam(required = false) String tenHoacSdt,
                          @RequestParam(required = false) String sdt,
                          @RequestParam(required = false) int trangthai,
                          @RequestParam(required = false) Date ngaysinh
                          ){
        List<KhachHang> lstTimKiem = khachHangService.findByAll(tenHoacSdt, sdt, trangthai, ngaysinh);
        model.addAttribute("lstKhachHang", lstTimKiem);
        model.addAttribute("lstNguoiDung", nguoiDungService.findAll());
        return "admin/qlkhachhang";
    }

    @GetMapping("/capnhat/{id}")
    public String hienThiCapNhat(Model model,
                                 @PathVariable("id") int id
                                 ) {
        KhachHang khachHang = khachHangService.getOne(id);
        model.addAttribute("getone", khachHang);
        return "admin/updatekhachhang";
    }

}
