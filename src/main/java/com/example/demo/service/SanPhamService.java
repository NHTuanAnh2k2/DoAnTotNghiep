package com.example.demo.service;

import com.example.demo.entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SanPhamService {

    Page<SanPham> getAll(Pageable pageable);

    Page<SanPham> findAllByTensanphamOrTrangthai(String tensanpham, Boolean trangthai, Pageable pageable);

}
