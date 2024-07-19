package com.example.demo.service;


import com.example.demo.entity.DiaChi;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungNVInfo;
import com.example.demo.info.NhanVienSearch;

import java.sql.Date;
import java.util.List;

public interface NguoiDungService1 {
    List<NguoiDung> getAll();
    public NguoiDung add(NguoiDungNVInfo nguoiDung);
    public NguoiDung update(NguoiDungNVInfo nguoiDung, Integer id);
    public NguoiDung updateS(NguoiDung nd);
    NguoiDung search(String id);
    NguoiDung findById(Integer id);
    public void sendEmail(String to, String subject, String mailType, String mailContent);
}
