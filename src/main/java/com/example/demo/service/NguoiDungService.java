package com.example.demo.service;

import com.example.demo.entity.NguoiDung;

import java.util.List;

public interface NguoiDungService {
    NguoiDung findByTaiKhoan(String taikhoan);

    List<NguoiDung> findAll();
}
