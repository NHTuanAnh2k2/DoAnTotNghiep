package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.repository.NguoiDungRepository1;
import com.example.demo.service.NguoiDungService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.Normalizer;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class NguoiDungImpl1 implements NguoiDungService1 {
    @Autowired
    NguoiDungRepository1 nguoiDungRepository;

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();
    @Override
    public List<NguoiDung> getAll() {
        return nguoiDungRepository.getAllByOrderByIdDesc();
    }
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    public String generateRandomPassword(int length) {
        if (length < 4) throw new IllegalArgumentException("Length too short, minimum 4 characters required");
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(PASSWORD_ALLOW_BASE.charAt(random.nextInt(PASSWORD_ALLOW_BASE.length())));
        }
        return password.toString();
    }

    @Override
    public NguoiDung add(NguoiDungNVInfo nguoiDung) {
        List<NguoiDung> l = nguoiDungRepository.getAllByOrderByIdDesc();
        NguoiDung nd = new NguoiDung();
        nd.setTaikhoan(nguoiDung.getTaikhoan());
        nd.setEmail(nguoiDung.getEmail());
        nd.setMatkhau(passwordEncoder.encode(generateRandomPassword(10)));
        nd.setHovaten(nguoiDung.getHovaten());
        nd.setNgaysinh(nguoiDung.getNgaysinh());
        nd.setCccd(nguoiDung.getCccd());
        nd.setSodienthoai(nguoiDung.getSodienthoai());
        nd.setGioitinh(nguoiDung.getGioitinh());
        nd.setAnh(nguoiDung.getAnh());
        nd.setNgaytao(new Timestamp(new Date().getTime()));
        nd.setNguoitao("a");
        nd.setNguoicapnhat("a");
        nd.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        nd.setTrangthai(true);
        return nguoiDungRepository.save(nd);
    }

    @Override
    public NguoiDung update(NguoiDungNVInfo nguoiDung, Integer id) {
        NguoiDung nd = nguoiDungRepository.searchId(id);
        nd.setEmail(nguoiDung.getEmail());
        nd.setHovaten(nguoiDung.getHovaten());
        nd.setNgaysinh(nguoiDung.getNgaysinh());
        nd.setCccd(nguoiDung.getCccd());
        nd.setSodienthoai(nguoiDung.getSodienthoai());
        nd.setGioitinh(nguoiDung.getGioitinh());
        nd.setAnh(nguoiDung.getAnh());
        nd.setNguoicapnhat("a");
        nd.setLancapnhatcuoi(new Timestamp(new Date().getTime()));
        return nguoiDungRepository.save(nd);
    }

    @Override
    public NguoiDung updateS(NguoiDung nd) {
        return nguoiDungRepository.save(nd);
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String mailType, String mailContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(mailType);
        message.setText(mailContent);
//        message.setText(a);
        emailSender.send(message);
    }

    @Override
    public NguoiDung search(String id) {
        return nguoiDungRepository.searchEmail(id);
    }

    @Override
    public NguoiDung findById(Integer id) {
        return nguoiDungRepository.searchId(id);
    }
}
