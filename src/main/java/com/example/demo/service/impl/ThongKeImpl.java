package com.example.demo.service.impl;

import com.example.demo.repository.ThongKeRepository;
import com.example.demo.repository.hoadon.HoaDonRepository;
import com.example.demo.service.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Map<String, Object> dayData() {
//        // Tạo định dạng cho ngày
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
//
//        // Tính toán ngày từ 7 ngày trước đến hôm nay
//        List<String> labels = new ArrayList<>();
//        LocalDate today = LocalDate.now();
//        for (int i = 6; i >= 0; i--) {
//            LocalDate date = today.minusDays(i);
//            labels.add(date.format(formatter));
//        }
//
//        // Truy vấn cơ sở dữ liệu để lấy dữ liệu
//        List<Integer> productData = dao.getProductDataForLast7Days();
//        List<Integer> invoiceData = dao.getInvoiceDataForLast7Days();
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("labels", labels.toArray(new String[0]));
//        data.put("datasets", new int[][]{
//                productData.stream().mapToInt(Integer::intValue).toArray(),  // Dữ liệu sản phẩm
//                invoiceData.stream().mapToInt(Integer::intValue).toArray()   // Dữ liệu hóa đơn
//        });
//        return data;
        Map<String, Object> data = new HashMap<>();
        data.put("labels", new String[]{"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"});
        data.put("datasets", new int[][]{
                {18, 12, 6, 9, 12, 3, 9},  // Dữ liệu sản phẩm
                {10, 70, 1, 5, 7, 4, 15}   // Dữ liệu hóa đơn
        });
        return data;
    }

    @Override
    public Map<String, Object> thangData() {
        // Trả về dữ liệu của tháng
        Map<String, Object> data = new HashMap<>();
        data.put("labels", new String[]{"Week 1", "Week 2", "Week 3", "Week 4"});
        data.put("datasets", new int[][]{
                {80, 90, 100, 110},  // Dữ liệu sản phẩm
                {50, 60, 70, 80}     // Dữ liệu hóa đơn
        });
        return data;
    }

    @Override
    public Map<String, Object> namData() {
        Map<String, Object> data = new HashMap<>();
        data.put("labels", new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        data.put("datasets", new int[][]{
                {300, 400, 350, 500, 450, 600, 700, 800, 750, 900, 950, 1000},  // Dữ liệu sản phẩm
                {200, 300, 250, 350, 300, 400, 500, 600, 550, 700, 750, 800}    // Dữ liệu hóa đơn
        });
        return data;
    }

    @Override
    public Map<String, Object> khoangngayData() {
        // Trả về dữ liệu tùy chỉnh
        Map<String, Object> data = new HashMap<>();
        data.put("labels", new String[]{"Custom1", "Custom2", "Custom3", "Custom4"});
        data.put("datasets", new int[][]{
                {50, 60, 70, 80},  // Dữ liệu sản phẩm
                {30, 40, 50, 60}   // Dữ liệu hóa đơn
        });
        return data;
    }
}
