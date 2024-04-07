package com.example.demo.service.impl;

import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienSearch;
import com.example.demo.repository.NguoiDungRepository1;
import com.example.demo.service.NguoiDungService1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Timestamp;
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
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+";

    public static String generatePassword(int length) {
        Random random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            password.append(CHARACTERS.charAt(index));
        }
        return password.toString();
    }
    public String tk(){
        List<NguoiDung> l = nguoiDungRepository.getAllByOrderByIdDesc();
        String tk = "NV" + l.size();
        return tk;
    }

    @Override
    public NguoiDung add(NguoiDungNVInfo nguoiDung) {
        List<NguoiDung> l = nguoiDungRepository.getAllByOrderByIdDesc();
        NguoiDung nd = new NguoiDung();
        nd.setTaikhoan(tk());
        nd.setEmail(nguoiDung.getEmail());
        nd.setMatkhau(generatePassword(8));
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
        nd.setTrangthai(true);
        return nguoiDungRepository.save(nd);
    }

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
    public List<NguoiDung> searchND(String ten, Boolean trangThai, java.sql.Date batDau, java.sql.Date ketThuc) {
        return nguoiDungRepository.findByKey(ten,batDau,ketThuc,trangThai);
    }

    @Override
    public List<NguoiDung> searchkey(NhanVienSearch nhanVienSearch) {
        return nguoiDungRepository.findByKeys(nhanVienSearch.getKey(),nhanVienSearch.isTrangThai());
    }

    @Override
    public List<NguoiDung> searchStart(String ten, Boolean trangThai, java.sql.Date batDau) {
        return nguoiDungRepository.findByStart(ten,batDau,trangThai);
    }

    @Override
    public List<NguoiDung> searchEnd(String ten, Boolean trangThai, java.sql.Date ketThuc) {
        return nguoiDungRepository.findByEnd(ten,ketThuc,trangThai);
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
