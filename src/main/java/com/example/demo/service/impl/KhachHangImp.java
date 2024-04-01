package com.example.demo.service.impl;

import com.example.demo.entity.KhachHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class KhachHangImp implements KhachHangService {
    @Autowired
    KhachHangRepostory khachHangRepostory;
    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @Override
    public List<KhachHang> findAll() {
        return khachHangRepostory.findAll();
    }

    @Override
    public KhachHang add(KhachHang khachHang) {
        khachHangRepostory.save(khachHang);
        return khachHang;
    }

    @Override
    public List<KhachHang> findByAll(String ten, String sdt, int trangthai, Date ngaysinh) {
        return khachHangRepostory.findByAll(ten, sdt, trangthai, ngaysinh);
    }

    @Override
    public KhachHang getOne(int id) {
        return khachHangRepostory.getReferenceById(id);
    }
}
