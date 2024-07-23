package com.example.demo.controller.login;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.AdminTokenInfo;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.info.TaiKhoanTokenInfo;
import com.example.demo.info.token.AdminManager;
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
import org.springframework.http.HttpRequest;
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

import java.util.ArrayList;
import java.util.List;

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
    public AdminManager adminManager;
    @Autowired
    JwtResponseDTO jwtResponseDTO;

    @GetMapping("/account")
    public String displaydangnhap(Model model) {
        model.addAttribute("nguoidung", new NguoiDung());
        model.addAttribute("dangnhap", new AuthRequestDTO());
        return "admin/dangnhap/loginadmin";
    }

    public List<AdminTokenInfo> adminTokenInfos = new ArrayList<>();
    @PostMapping("/dangnhap")
    public String dangnhap(Model model,
                           @RequestParam("username") String taikhoan,
                           @RequestParam("password") String matkhau,
                           @ModelAttribute("dangnhap") AuthRequestDTO dangnhap,
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request,
                           HttpSession session
    ) {
        if (taikhoan == "" && matkhau == "") {
            redirectAttributes.addFlashAttribute("error", "Tài khoản và mật khẩu đang trống");
            return "redirect:/admin/account";
        } else if (taikhoan == "") {
            redirectAttributes.addFlashAttribute("error", "Tài khoản đang trống");
            return "redirect:/admin/account";
        } else if (matkhau == "") {
            redirectAttributes.addFlashAttribute("error", "Mật khẩu đang trống");
            return "redirect:/admin/account";
        } else {
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
                if (nv.getTrangthai() == false) {
                    redirectAttributes.addFlashAttribute("error", "Tài khoản đã bị khóa");
                    return "redirect:/admin/account";
                }

                if (passwordEncoder.matches(matkhau, userDetails.getPassword())) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    String token = jwtGenerator.generateToken(authentication);
                    Integer userId = nd.getId();
                    AdminTokenInfo adminTokenInfo = new AdminTokenInfo(userId, token);
                    adminTokenInfos.add(adminTokenInfo);
                    session.setAttribute("taiKhoanTokenInfos", adminTokenInfos);
                    adminManager.addUser(nd.getId(), token);
                    session.setAttribute("tokenAdmin", token);
                    session.setAttribute("adminDangnhap", nd.getTaikhoan());
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

    @GetMapping("/dangxuat")
    public String logout(HttpServletRequest request, HttpSession session) {
        // Xóa session của người dùng để đăng xuất
        String userName = (String) session.getAttribute("adminDangnhap");
        NguoiDung nguoiDung = khachHangService.findNguoiDungByTaikhoan(userName);
        Integer adminId = nguoiDung.getId();
        String token = adminManager.getToken(adminId);
        session.removeAttribute("adminDangnhap");
        session.removeAttribute("adminToken");
        session.invalidate();
        adminManager.logoutUser(adminId, token);
        return "redirect:/admin/account";

    }
}
