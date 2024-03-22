package com.example.demo.service.impl;

import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamChiTietRepository;
import com.example.demo.service.SanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamChiTietImp implements SanPhamChiTietService {
    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;


    @Override
    public List<SanPhamChiTiet> findAll() {
       return sanPhamChiTietRepository.findAll();
    }
}
