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
    @Override
    public List<NguoiDung> getAll() {
        return nguoiDungRepository.getAllByOrderByIdDesc();
    }



    @Override
    public NguoiDung add(NguoiDungNVInfo nguoiDung) {
        List<NguoiDung> l = nguoiDungRepository.getAllByOrderByIdDesc();
        NguoiDung nd = new NguoiDung();
        nd.setTaikhoan(nguoiDung.getTaikhoan());
        nd.setEmail(nguoiDung.getEmail());
        nd.setMatkhau(nguoiDung.getMatkhau());
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
