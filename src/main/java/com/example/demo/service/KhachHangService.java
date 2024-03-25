package com.example.demo.service;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;

import java.util.List;

public interface KhachHangService {
    List<KhachHang> findAll();
    KhachHang add(KhachHang khachHang);

}
