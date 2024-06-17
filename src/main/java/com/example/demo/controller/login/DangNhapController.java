package com.example.demo.controller.login;

import com.example.demo.entity.NguoiDung;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.security.CustomerUserDetailService;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DangNhapController {
    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungService nguoiDungService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private CustomerUserDetailService customerUserDetailService;
    @Autowired
    private JWTGenerator jwtGenerator;

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
            if (userDetails == null) {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                return "redirect:/dangky";
            }

            if (passwordEncoder.matches(matkhau, userDetails.getPassword())) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtGenerator.generateToken(authentication);
                session.setAttribute("userDangnhap", nd.getTaikhoan());
                return "redirect:/customer/trangchu";
            } else {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                System.out.println("Mật khẩu không đúng");
                return "redirect:/dangky";
            }
        } catch (UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
            System.out.println("Sai tài khoản hoặc mật khẩu");
            return "redirect:/dangky";
        } catch (Exception e) {
            System.out.println(e);
            redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại");
            return "redirect:/dangky";
        }
    }

}
