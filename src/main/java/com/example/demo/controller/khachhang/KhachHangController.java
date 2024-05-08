package com.example.demo.controller.khachhang;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.*;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.restcontroller.khachhang.KhachHangRestController;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.service.DiaChiService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    NguoiDungRepository nguoiDungRepository;

    @GetMapping
    public String display(Model model, @ModelAttribute("khachhang") KhachHang khachHang) {
        model.addAttribute("lstKhachHang", khachHangService.displayKhachHang());
        return "admin/qlkhachhang";
    }

    @GetMapping("/timkiem")
    public String search(@RequestParam(value = "tenSdtMa", required = false) String tenSdtMa,
                         Model model
    ) {
        List<KhachHangInfo> lstKhachHang = new ArrayList<>();
        if (tenSdtMa != null) {
            lstKhachHang = khachHangService.findByTenSdtMa(tenSdtMa);
        }
        model.addAttribute("lstKhachHang", lstKhachHang);
        return "admin/qlkhachhang";
    }

    @GetMapping("/add")
    public String form(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                       @ModelAttribute("diachi") DiaChiKHInfo diachi,
                       Model model
    ) {
        List<Province> cities = khachHangService.getCities();
        model.addAttribute("cities", cities);
        return "admin/addkhachhang";
    }

    @PostMapping("/add")
    public String add(@Valid @ModelAttribute("khachhang") KhachHang khachhang,
                      BindingResult khBindingResult,
                      @Valid @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                      BindingResult ndBindingResult,
                      @Valid @ModelAttribute("diachi") DiaChiKHInfo diachi,
                      BindingResult dcBindingResult,
                      Model model
    ) throws IOException {
        List<Province> cities = khachHangService.getCities();
        if (khBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "admin/addkhachhang";
        } else if (ndBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "admin/addkhachhang";
        } else if (dcBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "admin/addkhachhang";
        }

        LocalDateTime currentDate = LocalDateTime.now();
        int usernameLength = 8;
        int passwordLength = 10;
        String username = khachHangService.generateRandomPassword(usernameLength);
        String password = khachHangService.generateRandomPassword(passwordLength);

        NguoiDung nd = new NguoiDung();
        nd.setEmail(nguoidung.getEmail());
        nd.setHovaten(nguoidung.getHovaten());
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setCccd(nguoidung.getCccd());
        nd.setSodienthoai(nguoidung.getSodienthoai());
        nd.setGioitinh(nguoidung.getGioitinh());
        nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nd.setTaikhoan(username);
        nd.setMatkhau(password);
        nd.setNgaytao(Timestamp.valueOf(currentDate));
        nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nd.setTrangthai(true);
        khachHangService.addNguoiDung(nd);

        DiaChi dc = new DiaChi();
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong());
        dc.setNguoidung(nd);
        dc.setHotennguoinhan(nd.getHovaten());
        dc.setSdtnguoinhan(nd.getSodienthoai());
        dc.setTrangthai(nd.getTrangthai());
        dc.setNgaytao(nd.getNgaytao());
        dc.setLancapnhatcuoi(nd.getLancapnhatcuoi());
        khachHangService.addDiaChi(dc);

        KhachHang kh = new KhachHang();
        List<KhachHang> lstKhachHang = khachHangService.findAllKhachHang();
        int total = lstKhachHang.size();
        String maKH = "KH" + (total + 1);
        kh.setMakhachhang(maKH);
        kh.setNguoidung(nd);
        kh.setTrangthai(nd.getTrangthai());
        kh.setNgaytao(nd.getNgaytao());
        kh.setLancapnhatcuoi(nd.getLancapnhatcuoi());
        khachHangService.addKhachHang(kh);

        khachHangService.sendEmail(nd.getEmail(), nd.getTaikhoan(), nd.getMatkhau(), nd.getHovaten());

        model.addAttribute("cities", cities);
        return "redirect:/khachhang/add";
    }

    @GetMapping("/update/{id}")
    public String displayUpdate(@ModelAttribute("diachi") DiaChiKHInfo diaChi,
                                @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                                @PathVariable("id") Integer id,
                                Model model) {

        DiaChi diaChiSelect = khachHangService.findDiaChiById(id);
        NguoiDung nguoiDungSelect = khachHangService.findNguoiDungById(diaChiSelect.getNguoidung().getId());
        List<Province> cities = khachHangService.getCities();
        model.addAttribute("cities", cities);
        model.addAttribute("diachi", diaChiSelect);
        model.addAttribute("nguoidung", nguoiDungSelect);

        return "admin/updatekhachhang";
    }

    @PostMapping("/update/{id}")
    public String update(@Valid @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                         BindingResult ndBindingResult,
                         @Valid @ModelAttribute("diachi") DiaChiKHInfo diachi,
                         BindingResult dcBindingResult,
                         @PathVariable("id") Integer id,
                         Model model
    ) {
        List<Province> cities = khachHangService.getCities();
        if (ndBindingResult.hasErrors()) {
            List<ObjectError> lst = ndBindingResult.getAllErrors();
            System.out.println(lst);
            model.addAttribute("cities", cities);
            return "admin/updatekhachhang";
        } else if (dcBindingResult.hasErrors()) {
            List<ObjectError> lst = dcBindingResult.getAllErrors();
            System.out.println(lst);
            model.addAttribute("cities", cities);
            return "admin/updatekhachhang";
        }

        DiaChi dc = khachHangService.findDiaChiById(id);
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
        dc.setQuanhuyen(diachi.getQuanhuyen());
        dc.setXaphuong(diachi.getXaphuong());
        dc.setTenduong(diachi.getTenduong());
        dc.setLancapnhatcuoi(Timestamp.valueOf(LocalDateTime.now()));
        khachHangService.updateDiaChi(dc);

        NguoiDung nd = khachHangService.findNguoiDungById(dc.getNguoidung().getId());
        nd.setEmail(nguoidung.getEmail());
        nd.setHovaten(nguoidung.getHovaten());
        nd.setNgaysinh(nguoidung.getNgaysinh());
        nd.setCccd(nguoidung.getCccd());
        nd.setSodienthoai(nguoidung.getSodienthoai());
        nd.setGioitinh(nguoidung.getGioitinh());
        nd.setLancapnhatcuoi(dc.getLancapnhatcuoi());
        khachHangService.updateNguoiDung(nd);

        KhachHang kh = khachHangService.findKhachHangByIdNguoiDung(nd.getId());
        System.out.println(kh);
        kh.setLancapnhatcuoi(dc.getLancapnhatcuoi());
        khachHangService.updateKhachHang(kh);

        model.addAttribute("lstKhachHang", khachHangService.displayKhachHang());
        return "redirect:/khachhang";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id,
                         @ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                         @ModelAttribute("khachhang") KhachHangKHInfo khachhang,
                         @ModelAttribute("diachi") DiaChiKHInfo diachi,
                         Model model
                         ) {
        DiaChi diachiSelect = khachHangService.findDiaChiById(id);
        NguoiDung nguoidungSelect = khachHangService.findNguoiDungById(diachiSelect.getNguoidung().getId());
        KhachHang khachhangSelect = khachHangService.findKhachHangByIdNguoiDung(nguoidungSelect.getId());
        model.addAttribute("nguoidung", nguoidungSelect);
        model.addAttribute("diachi", diachiSelect);
        model.addAttribute("khachhang", khachhangSelect);

        return "admin/detailkhachhang";
    }

}
