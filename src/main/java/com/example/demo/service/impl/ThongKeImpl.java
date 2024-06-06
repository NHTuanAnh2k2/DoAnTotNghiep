package com.example.demo.service.impl;

import com.example.demo.repository.ThongKeRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class ThongKeImpl implements ThongKeService {
    @Autowired
    ThongKeRepository dao;

    @Override
    public int thongKeTheoThang() {
        return dao.thongKeTheoThang();
    }
    @Override
    public int thongKeTienTheoThang() {
        return dao.thongKeTienTheoThang();
    }
    @Override
    public int thongKeTheoNgay() {
        return dao.thongKeTheoNgay();
    }
    @Override
    public int thongKeTienTheoNgay() {
        return dao.thongKeTienTheoNgay();
    }

    @Override
    public List<Object[]> bdTron() {
        return dao.bdTron();
    }

    @Override
    public BigDecimal ttdsn() {
        return dao.ttdsn();
    }

    @Override
    public BigDecimal ttdst() {
        return dao.ttdst();
    }

    @Override
    public int ttspn() {
        return dao.ttspn();
    }

    @Override
    public int ttspt() {
        return dao.ttspt();
    }

    @Override
    public int tthdn() {
        return dao.tthdn();
    }

    @Override
    public int tthdt() {
        return dao.tthdt();
    }

    @Override
    public int ptdsn() {
        return dao.ptdtn();
    }

    @Override
    public int ptdst() {
        return dao.ptdtt();
    }

    @Override
    public int ptspn() {
        return dao.ptspn();
    }

    @Override
    public int ptspt() {
        return dao.ptspt();
    }

    @Override
    public int pthdn() {
        return dao.pthdn();
    }

    @Override
    public int pthdt() {
        return dao.pthdt();
    }
    @Override
    public int soLuongsp(){
        return dao.soLuongsp();
    }

    @Override
    public List<Object[]> soLuongDaBan() {
        return dao.soLuongDaBan();
    }
    @Override
    public List<Object[]> soLuongTon() {
        return dao.soLuongTon();
    }
}
