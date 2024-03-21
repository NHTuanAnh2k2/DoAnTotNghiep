package com.example.demo.service;

import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

public interface SanPhamService {
    Page<SanPham> getAll(Pageable pageable);

    Page<SanPham> findAllByTensanphamAndTrangthaiAndSpctSoluong(String tensanpham, Boolean trangthai, Integer soluong, Pageable pageable);

}
