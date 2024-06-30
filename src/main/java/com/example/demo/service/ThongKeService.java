package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ThongKeService {

    int thongKeTheoThang();
    int thongKeTienTheoThang();
    int thongKeTheoNgay();
    int thongKeTienTheoNgay();
    List<Object[]> bdTron();
    BigDecimal ttdsn();
    BigDecimal ttdst();
    int ttspn();
    int ttspt();
    int tthdn();
    int tthdt();
    int ptdsn();
    int ptdst();
    int ptspn();
    int ptspt();
    int pthdn();
    int pthdt();
    int soLuongsp();
    List<Object[]> soLuongDaBan();
    List<Object[]> soLuongTon();
    public List<Object[]> dayData();
    public List<Object[]> thangData();
    public List<Object[]> namData();
    public Map<String, Object> khoangngayData();
    public List<Object[]> khoangngay(Date startDate, Date endDate);

}
