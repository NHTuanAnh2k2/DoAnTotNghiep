package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.entity.PhuongThucThanhToan;

import java.util.List;

public interface PhuongThucThanhToanService {
    List<PhuongThucThanhToan> timTheoHoaDon(HoaDon hd);
}
