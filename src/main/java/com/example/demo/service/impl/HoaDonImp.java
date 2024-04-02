package com.example.demo.service.impl;

import com.example.demo.entity.HoaDon;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class HoaDonImp implements HoaDonService {
    @Autowired
    HoaDonRepository dao;

    @Override
    public Page<HoaDon> findAll(Pageable p) {
        return dao.findAll(p);
    }

    @Override
    public Page<HoaDon> Loc(Integer trangThai, Boolean loaihd, Date tu, Date den, Pageable p) {
        return dao.findAllByTrangthaiAndLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(trangThai, loaihd, tu, den, p);
    }

    @Override
    public Page<HoaDon> timKiemTT(Integer trangThai, Pageable p) {
        return dao.findAllByTrangthai(trangThai, p);
    }

    @Override
    public Long tinhTong(Integer tt) {
        return dao.countAllByTrangthai(tt);
    }

    @Override
    public List<HoaDon> timall() {
        return dao.findAll();
    }

    @Override
    public Page<HoaDon> LockTT(Boolean loaihd, Date tu, Date den, Pageable p) {
        return dao.findAllByLoaihoadonAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(loaihd,tu,den,p);
    }

    @Override
    public Page<HoaDon> LocKLHD(Integer trangThai, Date tu, Date den, Pageable p) {
        return dao.findAllByTrangthaiAndNgaytaoGreaterThanEqualAndNgaytaoLessThanEqual(trangThai,tu,den,p);
    }

    @Override
    public Page<HoaDon> LocKngayTao(Integer trangThai, Boolean loaihd, Pageable p) {
        return dao.findAllByTrangthaiAndLoaihoadon(trangThai,loaihd,p);
    }
}
