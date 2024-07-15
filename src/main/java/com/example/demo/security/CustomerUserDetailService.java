package com.example.demo.security;

import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.NhanVien;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhanVienRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    NguoiDungRepository nguoiDungRepository;
    @Autowired
    NhanVienRepository nhanVienRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findNguoiDungByTaikhoan(username);
        NhanVien nhanVien = nhanVienRepository.findNhanVienByIdNd(nguoiDung.getId());
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (nhanVien != null) {
            String role = nhanVien.getVaitro() == true ? "QUANLY" : "NHANVIEN";
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            return new User(nguoiDung.getTaikhoan(), nguoiDung.getMatkhau(), authorities);
        } else {
            return new User(nguoiDung.getTaikhoan(), nguoiDung.getMatkhau(), Collections.emptyList());
        }

    }

}
