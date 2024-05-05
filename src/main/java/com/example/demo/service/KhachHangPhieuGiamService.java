package com.example.demo.service;


import com.example.demo.entity.KhachHangPhieuGiam;
import com.example.demo.entity.PhieuGiamGia;

import java.util.List;

public interface KhachHangPhieuGiamService {
    KhachHangPhieuGiam AddKhachHangPhieuGiam(KhachHangPhieuGiam khachHangPhieuGiam);
    List<KhachHangPhieuGiam> findKhachHangPhieuGiamByIdPhieugiamgia(Integer IdPhieu);
}
