package com.example.demo.service;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Date;
import java.util.List;

public interface HoaDonChiTietService {
    Page<HoaDonChiTiet> getDSSPHD(HoaDon hd,Pageable p);
    List<HoaDonChiTiet> getListSPHD(HoaDon hd);
}
