package com.example.demo.service;

import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.SanPhamDotGiam;

import java.util.List;

public interface SanPhamDotGiamService {
    SanPhamDotGiam AddSanPhamDotGiam(SanPhamDotGiam sanPhamDotGiam);
    List<SanPhamDotGiam> findSanPhamDotGiamByIdDotgiamgia(Integer IdDot);
}
