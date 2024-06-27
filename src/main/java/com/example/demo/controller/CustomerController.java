package com.example.demo.controller;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.hosokhachhang.DoiMatKhauNguoiDung;
import com.example.demo.info.hosokhachhang.UpdateDiaChi;
import com.example.demo.info.hosokhachhang.UpdateKhachHang;
import com.example.demo.info.hosokhachhang.UpdateNguoiDung;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.restcontroller.khachhang.Province;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Controller
public class CustomerController {

    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    UserManager userManager;


    @GetMapping("/trangchu")
    public String trangchu() {
        return "customer/trangchu";
    }

    @GetMapping("/modal")
    public String modal() {
        return "customer/modal";
    }

    @GetMapping("/header")
    public String header(Model model,
                         HttpSession session
                         ) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        model.addAttribute("userDangnhap", userDangnhap);
        return "layout/header";
    }

    @GetMapping("/quenmatkhau")
    public String quenmatkhauview(@ModelAttribute("nguoidung") NguoiDungKHInfo nguoidung,
                                  Model model) {

        return "same/quenmatkhau";
    }

//    @PostMapping("/quenmatkhau")
//    public String quenmatkhau (@RequestParam("email") String email,
//                               @RequestParam("maxacnhan") String maxacnhan,
//                               Model model) {
//        NguoiDung nd = khachHangService.findByEmail(email);
//        if (nd != null) {
//            Random random = new Random();
//            int madoimatkhau = random.nextInt(900000) + 100000;
//            khachHangService.sendEmailQuenMatKhau(email, nd.getHovaten(), String.valueOf(madoimatkhau));
//            if (maxacnhan.equals(madoimatkhau)) {
//
//            }
//        }
//        return "same/quenmatkhau";
//    }

    @GetMapping("/hosokhachhang")
    public String hosokhachhang(Model model,
                                HttpSession session
                                ) {
        String userDangnhap = (String) session.getAttribute("userDangnhap");
        List<Province> cities = khachHangService.getCities();
        if (userDangnhap != null) {
            NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(userDangnhap);
            DiaChi dc = khachHangService.findDiaChiByIdNguoidung(nd.getId());
            model.addAttribute("nguoidung", nd);
            model.addAttribute("diachi", dc);
            model.addAttribute("userDangnhap", userDangnhap);
            model.addAttribute("nguoidungDoiMk", new DoiMatKhauNguoiDung());
            model.addAttribute("cities", cities);
            System.out.println("Danh sách người dùng đang đăng nhập: " + userManager.getLoggedInUsers());
            return "customer/hosokhachhang";
        } else {
            return "redirect:/dangky";
        }
    }

    @PostMapping("/capnhattaikhoan")
    public String capnhattaikhoan(Model model,
                                  @Valid @ModelAttribute("nguoidung") UpdateNguoiDung nguoidung,
                                  BindingResult ndBindingResult,
                                  @Valid @ModelAttribute("diachi") UpdateDiaChi diachi,
                                  BindingResult dcBindingResult,
                                  @Valid @ModelAttribute("khachhang")UpdateKhachHang khachhang,
                                  BindingResult khBindingResult
                                  ) {
        List<Province> cities = khachHangService.getCities();
        if (ndBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "customer/hosokhachhang";
        } else if (dcBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "customer/hosokhachhang";
        } else if (khBindingResult.hasErrors()) {
            model.addAttribute("cities", cities);
            return "customer/hosokhachhang";
        }


        return "redirect:/hosokhachhang";
    }

    @PostMapping("/doimatkhau/{id}")
    public String doimatkhau(@ModelAttribute("nguoidungDoiMk") DoiMatKhauNguoiDung nguoidung,
                             @PathVariable("id") Integer id,
                             Model model){
        NguoiDung nd = khachHangService.findNguoiDungById(id);
        String matkhauhientai = nd.getMatkhau();
        if (!matkhauhientai.equals(nguoidung.getMatkhau())) {
            model.addAttribute("error", "Mật khẩu hiện tại không đúng.");
            return "customer/hosokhachhang";
        }

        if (!nguoidung.getMatkhaumoi().equals(nguoidung.getMatkhaumoi())) {
            model.addAttribute("error", "Xác nhận mật khẩu mới không khớp.");
            return "customer/hosokhachhang";
        }
        khachHangService.doimatkhau(nguoidung, id);

        model.addAttribute("success", "Đổi mật khẩu thành công.");
        return "customer/hosokhachhang";
    }
}
