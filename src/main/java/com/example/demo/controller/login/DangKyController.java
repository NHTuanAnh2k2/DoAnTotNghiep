package com.example.demo.controller.login;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.*;
import com.example.demo.security.CustomerUserDetailService;
import com.example.demo.service.DiaChiService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DangKyController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    DiaChiService diaChiService;

    @GetMapping("/account")
    public String view(@ModelAttribute("nguoidung") DangKyNDInfo nguoidung,
                       @ModelAttribute("khachhang") DangKyKHInfo khachhang,
                       @ModelAttribute("dangnhap") DangNhapNDInfo dangnhap,
                       Model model
    ) {
        List<KhachHang> lstKh = khachHangService.findAll();
        List<String> lstEmail = new ArrayList<>();
        List<String> lstSdt = new ArrayList<>();
        List<String> lstTaikhoan = new ArrayList<>();
        for (KhachHang kh : lstKh) {
            lstEmail.add(kh.getNguoidung().getEmail());
            lstSdt.add(kh.getNguoidung().getSodienthoai());
            lstTaikhoan.add(kh.getNguoidung().getTaikhoan());
        }

        model.addAttribute("lstEmail", lstEmail);
        model.addAttribute("lstSdt", lstSdt);
        model.addAttribute("lstTaiKhoan", lstTaikhoan);
        return "customer/login";
    }

    @PostMapping("/dangky")
    public String dangky(@ModelAttribute("nguoidung") DangKyNDInfo nguoidung,
                         @ModelAttribute("khachhang") DangKyKHInfo khachhang,
                         @ModelAttribute("diachi") DangKyDCInfo diachi,
                         @ModelAttribute("dangnhap") DangNhapNDInfo dangnhap,
                         Model model
    ) {

//        if (ndBindingResult.hasErrors()) {
//            List<ObjectError> ndError = ndBindingResult.getAllErrors();
//            System.out.println(ndError);
//            return "customer/login";
//        } else if (khBindingResult.hasErrors()) {
//            List<ObjectError> khError = khBindingResult.getAllErrors();
//            System.out.println(khError);
//            return "customer/login";
//        } else if (dcBindingResult.hasErrors()) {
//            List<ObjectError> dcError = dcBindingResult.getAllErrors();
//            System.out.println(dcError);
//            return "customer/login";
//        }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = bCryptPasswordEncoder.encode(nguoidung.getMatkhau());


            //nguoi dung
            LocalDateTime currentDate = LocalDateTime.now();

            System.out.println(nguoidung.getTaikhoan());
            NguoiDung nd = new NguoiDung();
            nd.setHovaten(nguoidung.getHovaten());
            nd.setNgaysinh(nguoidung.getNgaysinh());
            nd.setGioitinh(nguoidung.getGioitinh());
            nd.setEmail(nguoidung.getEmail());
            nd.setSodienthoai(nguoidung.getSodienthoai());
            nd.setNgaytao(Timestamp.valueOf(currentDate));
            nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
            nd.setTaikhoan(nguoidung.getTaikhoan());
            nd.setMatkhau(encodedPassword);
            nd.setTrangthai(true);
            nd.setNguoitao("CUSTOMER");
            nd.setNguoicapnhat("CUSTOMER");
            khachHangService.addNguoiDung(nd);

            //Khach hang
            KhachHang kh = new KhachHang();
            List<KhachHang> lstKhachHang = khachHangService.findAllKhachHang();
            int total = lstKhachHang.size();
            String maKH = "KH" + (total + 1);
            kh.setMakhachhang(maKH);
            kh.setNguoitao(nd.getNguoitao());
            kh.setNguoicapnhat(nd.getNguoicapnhat());
            kh.setTrangthai(nd.getTrangthai());
            kh.setNguoidung(nd);
            kh.setNgaytao(nd.getNgaytao());
            kh.setLancapnhatcuoi(nd.getLancapnhatcuoi());
            khachHangService.addKhachHang(kh);

            DiaChi dc = new DiaChi();
            dc.setTenduong("");
            dc.setTinhthanhpho("");
            dc.setHotennguoinhan(nd.getHovaten());
            dc.setQuanhuyen("");
            dc.setXaphuong("");
            dc.setNguoidung(nd);
            dc.setLancapnhatcuoi(nd.getLancapnhatcuoi());
            dc.setNgaytao(nd.getNgaytao());
            dc.setNguoitao(nd.getNguoitao());
            dc.setNguoicapnhat(nd.getNguoicapnhat());
            dc.setSdtnguoinhan(nd.getSodienthoai());
            dc.setTrangthai(nd.getTrangthai());
            khachHangService.addDiaChi(dc);


//        khachHangService.sendEmail(nd.getEmail(), nd.getTaikhoan(), nd.getMatkhau(), hoten);

        return "redirect:/account";
    }

//    @PostMapping("/quenmatkhau")
//    public String quenmatkhau(@RequestParam("email") String email, Model model) {
//        NguoiDung nd = khachHangService.findByEmail(email);
//        SecureRandom random = new SecureRandom();
//        int CODE_LENGTH = 6;
//        if (nd != null) {
//            int code = random.nextInt((int) Math.pow(10, CODE_LENGTH));
//            khachHangService.sendEmailQuenMatKhau(email, nd.getHovaten(), String.valueOf(code));
//            model.addAttribute("dangnhap", true);
//            return "same/quenmatkhau";
//        } else {
//            model.addAttribute("message", "Email không tồn tại trong hệ thống.");
//            model.addAttribute("dangnhap", false);
//            return "same/quenmatkhau";
//        }
//    }


}
