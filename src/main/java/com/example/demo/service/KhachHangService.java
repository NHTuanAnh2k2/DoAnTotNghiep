package com.example.demo.service;

import com.example.demo.entity.KhachHang;

import java.util.Date;
import java.util.List;

public interface KhachHangService {
    List<KhachHang> findAll();
    KhachHang add(KhachHang khachHang);

    List<KhachHang> findByAll(String ten, String sdt, int trangthai, Date ngaysinh);

    KhachHang getOne(int id);
}
