package com.example.demo.controller.login;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.token.AuthRequestDTO;
import com.example.demo.info.token.JwtResponseDTO;
import com.example.demo.info.token.UserManager;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.security.CustomerUserDetailService;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.KhachHangService;
import com.example.demo.service.NguoiDungService;
import com.example.demo.service.NhanVienService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    JwtResponseDTO jwtResponseDTO;

    @GetMapping("/account")
    public String displaydangnhap(Model model) {
        model.addAttribute("nguoidung", new NguoiDung());
        model.addAttribute("dangnhap", new AuthRequestDTO());
        return "admin/dangnhap/loginadmin";
    }

    @PostMapping("/dangnhap")
    public String dangnhap(Model model,
                           HttpSession session,
                           @RequestParam("username") String taikhoan,
                           @RequestParam("password") String matkhau,
                           @ModelAttribute("dangnhap") AuthRequestDTO dangnhap,
                           RedirectAttributes redirectAttributes
                           ) {
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
                HttpHeaders headers = new HttpHeaders();
                headers.add("Authorization", "Bearer " + token);
                System.out.println(headers);
//                userManager.addUser(nd.getId(), token);
                jwtResponseDTO.setAccessToken(token);
                session.setAttribute("tokenAdmin", token);
                session.setAttribute("adminDangnhap", nd.getTaikhoan());
                model.addAttribute("tokenAdmin", token);
                return "redirect:/hoa-don/ban-hang";
            } else {
                redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
                return "redirect:/admin/account";
            }
        } catch (UsernameNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu");
            return "redirect:/admin/account";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Đã có lỗi xảy ra, vui lòng thử lại");
            return "redirect:/admin/account";
        }
    }
}
