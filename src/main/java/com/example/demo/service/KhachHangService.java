package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.info.KhachHangInfo;
import com.example.demo.info.NguoiDungKHInfo;
import com.example.demo.info.hosokhachhang.DoiMatKhauNguoiDung;
import com.example.demo.restcontroller.khachhang.Province;
import com.google.zxing.NotFoundException;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface KhachHangService {
    List<KhachHang> findAllKhachHang();
    DiaChi addDiaChi(DiaChi diaChi);
    KhachHang addKhachHang(KhachHang khachHang);
    NguoiDung addNguoiDung(NguoiDung nguoiDung);

    //Update khách hàng
    DiaChi updateDiaChi(DiaChi diaChi);
    KhachHang updateKhachHang(KhachHang khachHang);
    NguoiDung updateNguoiDung(NguoiDung nguoiDung);

    List<KhachHang> findAll();
    List<NguoiDung> findAllNguoiDung();
    KhachHang getOne(int id);
    KhachHang findKhachHangById(int id);
    DiaChi findDiaChiById(int id);
    NguoiDung findNguoiDungById(int id);
    List<Province> getCities();
    String generateRandomPassword(int length);
    void sendEmail(String recipient, String username, String password, String name);
    List<KhachHangInfo> displayKhachHang();
    KhachHang findKhachHangByIdNguoiDung(Integer id);

    //Tìm kiếm
    List<KhachHangInfo> findByTenSdtMa(String tenSdtMa);
    NguoiDung findByEmail(String email);
    NguoiDung findNguoiDungByTaikhoan(String taikhoan);
    DiaChi findDiaChiByIdNguoidung(Integer idNd);
    NguoiDung doimatkhau(DoiMatKhauNguoiDung nguoidung, int id);
    void scanQr() throws NotFoundException;
    String readQRCode(File qrCodeImage) throws IOException;
    List<KhachHang> findKHGanNhat();

    boolean sendPasswordResetCode(String email, String name);
    boolean validateResetCode(String email, String code);

}
