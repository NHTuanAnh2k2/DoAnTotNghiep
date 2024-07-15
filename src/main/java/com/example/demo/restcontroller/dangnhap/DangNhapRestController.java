package com.example.demo.restcontroller.dangnhap;

import com.example.demo.entity.KhachHang;
import com.example.demo.info.token.AuthRequestDTO;
import com.example.demo.info.token.JwtResponseDTO;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.security.CustomerUserDetailService;
import com.example.demo.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        @Autowired
        JWTGenerator jwtGenerator;
        @Autowired
        CustomerUserDetailService customerUserDetailService;
        @PostMapping("/dangnhap")
        public JwtResponseDTO dangnhap(@RequestBody AuthRequestDTO authRequestDTO) {
            UserDetails userDetails = customerUserDetailService.loadUserByUsername(authRequestDTO.getUsername());
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (authentication.isAuthenticated()) {
                System.out.println("thành công");
                return JwtResponseDTO.builder().accessToken(jwtGenerator.generateToken(authentication)).build();
            } else {
                throw new UsernameNotFoundException("Người dùng không hợp lệ");
            }
        }

    @GetMapping("/khachhang")
    public ResponseEntity<?> khachhang() {
        List<KhachHang> lstKH = khachHangRepostory.findAll();
        return ResponseEntity.ok(lstKH);
    }

}
