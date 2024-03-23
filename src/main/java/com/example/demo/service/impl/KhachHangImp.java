package com.example.demo.service.impl;

import com.example.demo.entity.KhachHang;
import com.example.demo.repository.khachhang.KhachHangRepostory;
import com.example.demo.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KhachHangImp implements KhachHangService {
    @Autowired
    KhachHangRepostory khachHangRepostory;

    @Override
    public List<KhachHang> findAll() {
        return khachHangRepostory.findAll();
    }
}
