package com.example.demo.restcontroller.dangnhap;

import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DangNhapRestController {

    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/dangnhap")
    public ResponseEntity<String> dangnhap(@RequestBody DangNhapNDInfo dangNhapNDInfo) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dangNhapNDInfo.getTaikhoan(),
                        dangNhapNDInfo.getMatkhau()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("Dang nhap thanh cong", HttpStatus.OK);
    }
}
