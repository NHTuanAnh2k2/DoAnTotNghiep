package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.KhachHangInfo;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.restcontroller.khachhang.Province;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHang> findAllKhachHang();

    KhachHang add(KhachHang khachHang, NguoiDung nguoiDung, DiaChi diaChi, String tinhthanhpho, String quanhuyen, String xaphuong, String tenduong);

    DiaChi updateDiaChi(DiaChi diaChi);
    KhachHang updateKhachHang(KhachHang khachHang);
    NguoiDung updateNguoiDung(NguoiDung nguoiDung);
    List<KhachHang> findAll();
    List<NguoiDung> findAllNguoiDung();

    List<KhachHang> findByAll(String ten, String sdt, Date ngaysinh);

    KhachHang getOne(int id);

    KhachHang findKhachHangById(int id);
    DiaChi findDiaChiById(int id);
    NguoiDung findNguoiDungById(int id);

    List<Province> getCities();

    String generateRandomPassword(int length);
    void sendEmail(String recipient, String username, String password, String name);

    List<KhachHangInfo> displayKhachHang();


    KhachHang findKhachHangByIdNguoiDung(Integer id);
}
