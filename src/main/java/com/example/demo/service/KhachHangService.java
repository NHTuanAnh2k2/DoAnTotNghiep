package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.restcontroller.khachhang.Province;

import java.sql.Date;
import java.util.List;

public interface KhachHangService {
    List<KhachHang> findAllKhachHang();

    KhachHang add(KhachHang khachHang, NguoiDung nguoiDung, DiaChi diaChi, String tinhthanhpho, String quanhuyen, String xaphuong, String tenduong);

    List<KhachHang> findAll();
    List<NguoiDung> findAllNguoiDung();

    List<KhachHang> findByAll(String ten, String sdt, Date ngaysinh);

    KhachHang getOne(int id);

    List<Province> getCities();

}
