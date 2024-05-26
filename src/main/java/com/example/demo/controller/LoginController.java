package com.example.demo.controller;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.*;
import com.example.demo.service.DiaChiService;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    DiaChiService diaChiService;
    @GetMapping("/same/login")
    public String display(@ModelAttribute("nguoidung") DangKyNDInfo nguoidung,
                          @ModelAttribute("diachi") DangKyDCInfo diachi,
                          @ModelAttribute("khachhang") DangKyKHInfo khachhang) {
        return "same/login";
    }

//    @PostMapping("/same/login")
//    public String login(
//            @RequestParam("taikhoan") String taikhoan,
//            @RequestParam("matkhau") String matkhau,
//            HttpSession session
//            ) {
//        NguoiDung nguoiDung = nguoiDungService.findByTaiKhoan(taikhoan);
//        if(nguoiDung != null && nguoiDung.getMatkhau().equals(matkhau)) {
//            session.setAttribute("tennguoidung", nguoiDung.getHovaten());
//            System.out.println(nguoiDung.getTaikhoan());
//            return "redirect:/trangchu";
//        } else {
//            return "redirect:/same/login";
//        }
//    }

    @PostMapping("/same/login")
    public String dangky(@Valid @ModelAttribute("nguoidung") DangKyNDInfo nguoidung,
                         BindingResult ndBindingResult,
                         @Valid @ModelAttribute("khachhang") DangKyKHInfo khachhang,
                         BindingResult khBindingResult,
                         @Valid @ModelAttribute("diachi") DangKyDCInfo diachi,
                         BindingResult dcBindingResult,
                         Model model
                         ){
        if (ndBindingResult.hasErrors()) {
            return "same/login";
        } else if (khBindingResult.hasErrors()) {
            return "same/login";
        } else if (dcBindingResult.hasErrors()) {
            return "same/login";
        }

        //nguoi dung
        LocalDateTime currentDate = LocalDateTime.now();
        int usernameLength = 8;
        int passwordLength = 10;
        String username = khachHangService.generateRandomPassword(usernameLength);
        String password = khachHangService.generateRandomPassword(passwordLength);
        String hoten = nguoidung.getHo() + " " + nguoidung.getTen();

        NguoiDung nd = new NguoiDung();
        nd.setHovaten(hoten);
        nd.setEmail(nguoidung.getEmail());
        nd.setSodienthoai(nguoidung.getSodienthoai());
        nd.setNgaytao(Timestamp.valueOf(currentDate));
        nd.setLancapnhatcuoi(Timestamp.valueOf(currentDate));
        nd.setTaikhoan(username);
        nd.setMatkhau(password);
        nd.setTrangthai(true);
        nd.setNguoitao("Customer");
        nd.setNguoicapnhat("Customer");
        nd.setNgaysinh(nguoidung.getNgaysinh());
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
        dc.setTenduong(diachi.getDiachi());
        dc.setTinhthanhpho(diachi.getTinhthanhpho());
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

        khachHangService.sendEmail(nd.getEmail(), nd.getTaikhoan(), nd.getMatkhau(), hoten);

        return "same/login";
    }

    @PostMapping("/quenmatkhau")
    public String quenmatkhau(@RequestParam("email") String email, Model model) {
        NguoiDung nd = khachHangService.findByEmail(email);
        if (nd != null) {
            String matkhaumoi = khachHangService.generateRandomPassword(10);
            nd.setMatkhau(matkhaumoi);
            khachHangService.updateNguoiDung(nd);
            khachHangService.sendEmailDoiMk(email, nd.getTaikhoan(), nd.getMatkhau(), nd.getHovaten());
            model.addAttribute("message", "Mật khẩu mới đã được gửi đến địa chỉ email của bạn");
            model.addAttribute("dangnhap", true);
            return "same/quenmatkhau";
        } else {
            model.addAttribute("message", "Email không tồn tại trong hệ thống.");
            model.addAttribute("dangnhap", false);
            return "same/quenmatkhau";
        }
    }

    @PostMapping("/dangnhap")
    public String login(@RequestParam("taikhoan") String taikhoan,
                        @RequestParam("matkhau") String matkhau,
                        HttpSession session,
                        Model model) {
        NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(taikhoan);
        if (nd != null && nd.getMatkhau().equals(matkhau)) {
            session.setAttribute("userDangnhap", nd.getTaikhoan());
            return "redirect:/trangchu";
        } else {
            model.addAttribute("dangnhapthatbai", "Sai tài khoản hoặct mật khẩu");
            return "same/login";
        }
    }
    @GetMapping("/dangxuat")
    public String logout(HttpServletRequest request) {
        // Xóa session của người dùng để đăng xuất
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/trangchu";
    }


}
