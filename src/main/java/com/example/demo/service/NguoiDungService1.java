package com.example.demo.service;


import com.example.demo.entity.NguoiDung;
import com.example.demo.info.NguoiDungNVInfo;

import java.util.List;

public interface NguoiDungService1 {
    List<NguoiDung> getAll();
    public NguoiDung add(NguoiDungNVInfo nguoiDung);
    NguoiDung search(String id);
    public void sendEmail(String to, String subject, String mailType, String mailContent);

}
