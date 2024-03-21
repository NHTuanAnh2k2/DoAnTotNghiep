package com.example.demo.service.impl;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.SanPhamRepositoty;
import com.example.demo.service.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SanPhamImp implements SanPhamService {
    @Autowired
    SanPhamRepositoty sanPhamRepositoty;


    @Override
    public Page<SanPham> getAll(Pageable pageable) {
        return sanPhamRepositoty.findAll(pageable);
    }

    @Override
    public Page<SanPham> findAllByTensanphamAndTrangthaiAndSpctSoluong(String tensanpham, Boolean trangthai, Integer soluong, Pageable pageable) {
        return sanPhamRepositoty.findAllByTensanphamAndTrangthaiAndSpctSoluong
                (tensanpham,trangthai,soluong,pageable);
    }


}
