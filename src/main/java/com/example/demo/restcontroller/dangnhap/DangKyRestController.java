package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.KhachHangService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DangKyRestController {

    @Autowired
    KhachHangService khachHangService;
    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    NhanVienRepository nhanVienRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/send-code")
    public ResponseEntity<String> sendPasswordResetCode(@RequestParam("emailResetPassword") String emailResetPassword,
                                                        HttpSession session) {

        KhachHang khachHang = khachHangRepostory.findKhachHangByEmail(emailResetPassword);
        Integer userId = khachHang.getNguoidung().getId();
        if (khachHang != null) {
            session.setAttribute("userIdKhachHang", userId);
            boolean success = khachHangService.sendPasswordResetCode(emailResetPassword, khachHang.getNguoidung().getHovaten());
            if (success) {
                System.out.println("Đã gửi mã đến mail");
                return ResponseEntity.ok("Mã khôi phục mật khẩu đã được gửi đến email của bạn.");
            } else {
                return ResponseEntity.badRequest().body("Email không tồn tại.");
            }
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy email");
        }
    }

    @PostMapping("/validate-code")
    public ResponseEntity<String> validateResetCode(@RequestParam("emailResetPassword") String emailResetPassword,
                                                    @RequestParam("codeReset") String code
//                                                    @RequestParam("userId") Integer userId
                                                    ) {
        boolean valid = khachHangService.validateResetCode(emailResetPassword, code);
        if (valid) {
            return ResponseEntity.ok("Mã xác nhận hợp lệ.");
        } else {
            return ResponseEntity.badRequest().body("Mã xác nhận không hợp lệ.");
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam("emailResetPassword") String emailResetPassword,
                                                 @RequestParam("newPassword") String newPassword) {
        KhachHang khachHang = khachHangRepostory.findKhachHangByEmail(emailResetPassword);

        if (khachHang != null) {
            khachHang.getNguoidung().setMatkhau(passwordEncoder.encode(newPassword));
            khachHangRepostory.save(khachHang);
            return ResponseEntity.ok("Đổi mật khẩu thành công.");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy email");
        }
    }

    @PostMapping("/send-code-admin")
    public ResponseEntity<String> sendPasswordResetCodeAdmin(@RequestParam("emailResetPassword") String emailResetPassword,
                                                             HttpSession session) {

        NhanVien nhanVien = nhanVienRepository.findNhanVienByEmail(emailResetPassword);
        Integer userId = nhanVien.getNguoidung().getId();

        if (nhanVien != null) {
            session.setAttribute("userIdNhanVien", userId);
            boolean success = khachHangService.sendPasswordResetCode(emailResetPassword, nhanVien.getNguoidung().getHovaten());
            if (success) {
                System.out.println("Đã gửi mã đến mail");
                return ResponseEntity.ok("Mã khôi phục mật khẩu đã được gửi đến email của bạn.");
            } else {
                return ResponseEntity.badRequest().body("Email không tồn tại.");
            }
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy email");
        }
    }

    @PostMapping("/change-password-admin")
    public ResponseEntity<String> changePasswordAdmin(@RequestParam("emailResetPassword") String emailResetPassword,
                                                      @RequestParam("newPassword") String newPassword) {
        NhanVien nhanVien = nhanVienRepository.findNhanVienByEmail(emailResetPassword);

        if (nhanVien != null) {
            nhanVien.getNguoidung().setMatkhau(passwordEncoder.encode(newPassword));
            nhanVienRepository.save(nhanVien);
            return ResponseEntity.ok("Đổi mật khẩu thành công.");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy email");
        }
    }

}
