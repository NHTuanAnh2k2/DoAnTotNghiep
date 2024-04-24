package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.entity.PhuongThucThanhToan;
import com.example.demo.repository.LichSuHoaDon.LichSuHoaDonRepository;
import com.example.demo.repository.phuongThucThanhToan.PhuongThucThanhToanRepository;
import com.example.demo.service.LichSuHoaDonService;
import com.example.demo.service.PhuongThucThanhToanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhuongThucThanhToanImp implements PhuongThucThanhToanService {
    @Autowired
    PhuongThucThanhToanRepository dao;

    @Override
    public List<PhuongThucThanhToan> timTheoHoaDon(HoaDon hd) {
        return dao.findAllByHoadon(hd);
    }
}
