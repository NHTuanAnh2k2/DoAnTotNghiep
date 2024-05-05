package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.repository.hoadon.HoaDonChiTietRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonChiTietService;
import com.example.demo.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class HoaDonChiTietImp implements HoaDonChiTietService {
    @Autowired
    HoaDonChiTietRepository dao;

    @Override
    public Page<HoaDonChiTiet> getDSSPHD(HoaDon hd, Pageable p) {
        return dao.findAllByHoadon(hd, p);
    }

    @Override
    public List<HoaDonChiTiet> getListSPHD(HoaDon hd) {
        return dao.findAllByHoadon(hd);
    }

    @Override
    public Boolean checkHDCT(HoaDon hd, SanPhamChiTiet spct) {
        return dao.existsByHoadonAndSanphamchitiet(hd, spct);
    }

    @Override
    public List<HoaDonChiTiet> timHDCT(HoaDon hd, SanPhamChiTiet spct) {
        return dao.findAllByHoadonAndSanphamchitiet(hd, spct);
    }

    @Override
    public void capnhat(HoaDonChiTiet hdct) {
        dao.save(hdct);
    }
}
