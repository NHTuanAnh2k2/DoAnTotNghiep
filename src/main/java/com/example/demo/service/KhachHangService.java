package com.example.demo.service;

import com.example.demo.entity.DiaChi;
import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface KhachHangService {
    List<KhachHang> findAllKhachHang();

    KhachHang add(KhachHang khachHang, NguoiDung nguoiDung, DiaChi diaChi, String tinhthanhpho, String quanhuyen);

    List<KhachHang> findAll();

    List<KhachHang> findByAll(String ten, String sdt, int trangthai, Date ngaysinh);

    KhachHang getOne(int id);

    List<String> getCities();

    List<Integer> getCityIds();

    List<String> getDistricts(Integer cityId);

    List<String> getWards(Integer wardId);
}
