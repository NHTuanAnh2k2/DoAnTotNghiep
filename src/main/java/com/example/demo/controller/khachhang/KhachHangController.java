package com.example.demo.controller.khachhang;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.KhachHangInfo;
import com.example.demo.restcontroller.khachhang.KhachHangRestController;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.service.DiaChiService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.*;

import java.io.IOException;

@Controller
@RequestMapping("khachhang")
public class KhachHangController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    DiaChiService diaChiService;
    @Autowired
    KhachHangRestController khachHangRestController;

    @GetMapping
    public String display(Model model, @ModelAttribute("khachhang") KhachHang khachHang) {
        List<KhachHang> lstKhachHang = khachHangService.findAllKhachHang();
        Collections.sort(lstKhachHang, Comparator.comparing(KhachHang::getNgaytao).reversed());

        List<DiaChi> lstDiaChi = diaChiService.getAll();
        Collections.sort(lstDiaChi, Comparator.comparing(DiaChi::getNgaytao).reversed());

        List<NguoiDung> lstNguoiDung = khachHangService.findAllNguoiDung();
        Collections.sort(lstNguoiDung, Comparator.comparing(NguoiDung::getNgaytao).reversed());

        List<KhachHangInfo> lstkhachhanginfo = new ArrayList<>();
        int minSize = Math.min(lstKhachHang.size(), Math.min(lstDiaChi.size(), lstNguoiDung.size()));
        for (int i = 0; i < minSize; i++) {
            KhachHangInfo khachHangInfo = new KhachHangInfo();
            khachHangInfo.setKhachhang(lstKhachHang.get(i));
            khachHangInfo.setDiachi(lstDiaChi.get(i));
            khachHangInfo.setNguoidung(lstNguoiDung.get(i));
            lstkhachhanginfo.add(khachHangInfo);
        }

        model.addAttribute("lstKhachHang", lstkhachhanginfo);
        return "admin/qlkhachhang";
    }

    @GetMapping("/timkiem")
    public String search(@RequestParam(value = "ten", required = false) String ten,
                         @RequestParam(value = "sdt", required = false) String sdt,
                         @RequestParam(value = "ngaysinh", required = false) Date ngaySinh,
                         Model model
    ) {
        List<KhachHang> lstTimKiem = khachHangService.findByAll(ten, sdt, ngaySinh);
        model.addAttribute("lstKhachHang", lstTimKiem);
        return "admin/qlkhachhang";
    }

    @GetMapping("/add")
    public String form(@ModelAttribute("nguoidung") NguoiDung nguoidung,
                       @ModelAttribute("diachi") DiaChi diachi,
                       Model model
    ) {

        List<Province> cities = khachHangService.getCities();
        model.addAttribute("cities", cities);
        return "admin/addkhachhang";
    }

    @PostMapping("/add")
    public String add(@Valid KhachHang khachHang,
                      @Valid NguoiDung nguoiDung,
                      @Valid DiaChi diaChi,
                      @RequestParam("tinhthanhpho") String tinhthanhpho,
                      @RequestParam("quanhuyen") String quanhuyen,
                      @RequestParam("xaphuong") String xaphuong,
                      @RequestParam("tenduong") String tenduong,
                      BindingResult bindingResult,
                      Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "redirect:/khachhang/add";
        }
        khachHangService.add(khachHang, nguoiDung, diaChi, tinhthanhpho, quanhuyen, xaphuong, tenduong);
        return "redirect:/khachhang/add";
    }


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
