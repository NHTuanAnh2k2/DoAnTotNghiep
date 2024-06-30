package com.example.demo.security;

import com.example.demo.entity.NguoiDung;
import com.example.demo.info.DangNhapNDInfo;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NguoiDung nguoiDung = nguoiDungRepository.findNguoiDungByTaikhoan(username);
        if (nguoiDung == null) {
            System.out.println("NguoiDung null");
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(nguoiDung.getMatkhau());
        System.out.println("PasswordEncode: " + encodedPassword);
        return new User(nguoiDung.getTaikhoan(), encodedPassword, Collections.emptyList());
    }
}
