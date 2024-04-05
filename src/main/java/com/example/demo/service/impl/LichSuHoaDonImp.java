package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import com.example.demo.repository.LichSuHoaDon.LichSuHoaDonRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import com.example.demo.service.LichSuHoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class LichSuHoaDonImp implements LichSuHoaDonService {
    @Autowired
    LichSuHoaDonRepository dao;

    @Override
    public List<LichSuHoaDon> timLichSuTheoIDHoaDon(HoaDon hd) {
        return dao.findAllByHoadonOrderByNgaytao(hd);
    }
}
