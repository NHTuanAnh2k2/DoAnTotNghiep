package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.LichSuHoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface LichSuHoaDonService {
    List<LichSuHoaDon> timLichSuTheoIDHoaDon(HoaDon hd);
    public void add(LichSuHoaDon hd);
}
