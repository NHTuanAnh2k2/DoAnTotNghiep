package com.example.demo.service;

import com.example.demo.entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SanPhamChiTietService {

    List<SanPhamChiTiet> findAll();

    Page<SanPhamChiTiet> finAllPage(Pageable pageable);

    SanPhamChiTiet addSPCT(SanPhamChiTiet sanPhamChiTiet);
}
