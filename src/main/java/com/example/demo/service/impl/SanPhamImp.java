package com.example.demo.service.impl;

import com.example.demo.entity.SanPham;
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
    public List<SanPham> findAll() {
        return sanPhamRepositoty.findAll();
    }

    @Override
    public Page<SanPham> getAll(Pageable pageable) {
        return sanPhamRepositoty.findAll(pageable);
    }

    @Override
    public Page<SanPham> findAllByTensanphamOrTrangthai(String tensanpham, Boolean trangthai, Pageable pageable) {
        return sanPhamRepositoty.findAllByTensanphamOrTrangthai(tensanpham,trangthai,pageable);
    }

    @Override
    public SanPham add(SanPham sanPham) {
        return sanPhamRepositoty.save(sanPham);
    }


}
