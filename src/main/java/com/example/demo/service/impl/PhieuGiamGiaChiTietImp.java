package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.PhieuGiamGiaChiTiet;
import com.example.demo.entity.SanPhamChiTiet;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.PhieuGiamGiaChiTiet.PhieuGiamChiTietRepository;
import com.example.demo.repository.ThuongHieuRepository;
import com.example.demo.service.PhieuGiamGiaChiTietService;
import com.example.demo.service.ThuongHieuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhieuGiamGiaChiTietImp implements PhieuGiamGiaChiTietService {
    @Autowired
    PhieuGiamChiTietRepository dao;

    @Override
    public List<PhieuGiamGiaChiTiet> timListPhieuTheoHD(HoaDon hd) {
        return dao.findAllByHoadon(hd);
    }
}
