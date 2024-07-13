package com.example.demo.controller.login;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.security.CustomerUserDetailService;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import com.example.demo.service.NhanVienService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class DangNhapAdminController {

    @Autowired
    NhanVienRepository nhanVienRepository;
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private CustomerUserDetailService customerUserDetailService;
    @Autowired
    private JWTGenerator jwtGenerator;
    @Autowired
    public UserManager userManager;

    @GetMapping("/account")
    public String displaydangnhap() {
        return "admin/dangnhap/loginadmin";
    }

    @PostMapping("/dangnhap")
    public String dangnhap(Model model,
                           HttpSession session,
                           @RequestParam("taikhoan") String taikhoan,
                           @RequestParam("matkhau") String matkhau,
                           @ModelAttribute("dangnhap") DangNhapNDInfo dangnhap,
                           HttpServletRequest request,
                           RedirectAttributes redirectAttributes) {
        try {

            NguoiDung nd = khachHangService.findNguoiDungByTaikhoan(taikhoan);
            UserDetails userDetails = customerUserDetailService.loadUserByUsername(taikhoan);
            NhanVien nv = nhanVienRepository.findNhanVienByIdNd(nd.getId());

            if (userDetails == null) {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                return "redirect:/admin/account";
            }
            if (nv == null) {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                return "redirect:/admin/account";
            }

            if (passwordEncoder.matches(matkhau, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtGenerator.generateToken(authentication);
                session.setAttribute("tokenAdmin", token);
                return "redirect:/customer/trangchu";
            } else {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                return "redirect:/account";
            }
        } catch (UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "redirect:/account";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại");
            return "redirect:/account";
        }
    }
}
