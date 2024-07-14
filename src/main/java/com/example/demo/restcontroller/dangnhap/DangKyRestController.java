package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DangKyRestController {

    @Autowired
    KhachHangService khachHangService;
    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @PostMapping("/send-code")
        public ResponseEntity<String> sendPasswordResetCode(@RequestParam("emailResetPassword") String emailResetPassword) {

        NguoiDung nguoiDung = nguoiDungRepository.findNguoiDungByEmail(emailResetPassword);

        if (nguoiDung != null) {
            System.out.println("NguoiDung: " + nguoiDung.getHovaten());
            boolean success = khachHangService.sendPasswordResetCode(emailResetPassword, nguoiDung.getHovaten());
            if (success) {
                System.out.println("Đã gửi mã đến mail");
                return ResponseEntity.ok("Mã khôi phục mật khẩu đã được gửi đến email của bạn.");
            } else {
                return ResponseEntity.badRequest().body("Email không tồn tại.");
            }
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy nd");
        }
    }

    @PostMapping("/validate-code")
    public ResponseEntity<String> validateResetCode(@RequestParam("emailResetPassword") String emailResetPassword,
                                                    @RequestParam("codeReset") String code) {
        boolean valid = khachHangService.validateResetCode(emailResetPassword, code);
        if (valid) {
            return ResponseEntity.ok("Mã xác nhận hợp lệ.");
        } else {
            return ResponseEntity.badRequest().body("Mã xác nhận không hợp lệ.");
        }
    }

//    @PostMapping("/reset-password")
//    public ResponseEntity<String> resetPassword(@RequestParam String newPassword, @RequestParam String confirmPassword) {
//        // Xử lý logic đặt lại mật khẩu mới
//        if (!newPassword.equals(confirmPassword)) {
//            return ResponseEntity.badRequest().body("Mật khẩu xác nhận không khớp.");
//        }
////        khachHangService.updateNguoiDung();
//        return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công.");
//    }
}
