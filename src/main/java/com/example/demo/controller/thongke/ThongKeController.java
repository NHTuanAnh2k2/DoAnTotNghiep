package com.example.demo.controller.thongke;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.service.impl.HoaDonChiTietImp;
import com.example.demo.service.impl.HoaDonImp;
import com.example.demo.service.impl.ThongKeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class ThongKeController {
    @Autowired
    ThongKeImpl thongKe;

    @GetMapping("/admin/qlthongke")
    public String admin(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        int slhdtt = thongKe.thongKeTheoThang();
        int ttt = thongKe.thongKeTienTheoThang();
        int slhdtn = thongKe.thongKeTheoNgay();
        int ttn = thongKe.thongKeTienTheoNgay();
        int sptt = thongKe.soLuongsp();
        List<Object[]>  slt = thongKe.soLuongTon();
        BigDecimal ttdsn = thongKe.ttdsn();
        BigDecimal ttdst = thongKe.ttdst();
        int ttspn = thongKe.ttspn();
        int ttspt = thongKe.ttspt();
        int tthdn = thongKe.tthdn();
        int tthdt = thongKe.tthdt();
        int ptdsn = thongKe.ptdsn();
        int ptdst = thongKe.ptdst();
        int ptspn = thongKe.ptspn();
        int ptspt = thongKe.ptspt();
        int pthdn = thongKe.pthdn();
        int pthdt = thongKe.pthdt();
        List<Object[]> dayData = thongKe.dayData();
        List<Object[]> thangData = thongKe.thangData();
        List<Object[]> namData = thongKe.namData();
        Map<String, Object> khoangngayData = thongKe.khoangngayData();
        //xử lý dữ liệu biểu đồ tròn
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (Object[] row : thongKe.bdTron()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("value", row[1]);
            formattedData.add(map);
        }
        System.out.println(formattedData);
        //xử lý dữ liệu của biểu đồ cột theo ngày
        List<Map<String, Object>> formatteddayData = new ArrayList<>();
        for (Object[] row : thongKe.dayData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formatteddayData.add(map);
        }
        System.out.println(formatteddayData);
        //xử lý dữ liệu của biểu đồ cột theo tháng
        List<Map<String, Object>> formattedthangData = new ArrayList<>();
        for (Object[] row : thongKe.thangData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattedthangData.add(map);
        }
        System.out.println(formattedthangData);
        //xử lý dữ liệu của biểu đồ cột theo năm
        List<Map<String, Object>> formattednamData = new ArrayList<>();
        for (Object[] row : thongKe.namData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattednamData.add(map);
        }
        System.out.println(formattednamData);
        List<Map<String, Object>> formattednknData = new ArrayList<>();
        if(startDate != null && endDate != null) {
            System.out.println(startDate);
            System.out.println(endDate);

            for (Object[] row : thongKe.khoangngay(startDate, endDate)) {
                Map<String, Object> map = new HashMap<>();
                map.put("label", row[0]);
                map.put("sp", row[1]);
                map.put("hd", row[2]);
                formattednknData.add(map);
            }
            System.out.println(formattednknData);
        }

        model.addAttribute("bdTron", formattedData);
        model.addAttribute("slhdtt", slhdtt);
        model.addAttribute("ttt", ttt);
        model.addAttribute("slhdtn", slhdtn);
        model.addAttribute("ttn", ttn);
        model.addAttribute("sptt", sptt);
        model.addAttribute("sp", sp);
        model.addAttribute("slt", slt);
        model.addAttribute("ttdsn", ttdsn);
        model.addAttribute("ttdst", ttdst);
        model.addAttribute("ttspn", ttspn);
        model.addAttribute("ttspt", ttspt);
        model.addAttribute("tthdn", tthdn);
        model.addAttribute("tthdt", tthdt);
        model.addAttribute("ptdsn", ptdsn);
        model.addAttribute("ptdst", ptdst);
        model.addAttribute("ptspn", ptspn);
        model.addAttribute("ptspt", ptspt);
        model.addAttribute("pthdn", pthdn);
        model.addAttribute("pthdt", pthdt);
        model.addAttribute("dayData", formatteddayData);
        model.addAttribute("thangData", formattedthangData);
        model.addAttribute("namData", formattednamData);
        model.addAttribute("knData", formattednknData);
        return "admin/qlthongke";
    }

    @GetMapping("/khngay")
    public String admin1(
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
            Model model) {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        int slhdtt = thongKe.thongKeTheoThang();
        int ttt = thongKe.thongKeTienTheoThang();
        int slhdtn = thongKe.thongKeTheoNgay();
        int ttn = thongKe.thongKeTienTheoNgay();
        int sptt = thongKe.soLuongsp();
        List<Object[]>  slt = thongKe.soLuongTon();
        BigDecimal ttdsn = thongKe.ttdsn();
        BigDecimal ttdst = thongKe.ttdst();
        int ttspn = thongKe.ttspn();
        int ttspt = thongKe.ttspt();
        int tthdn = thongKe.tthdn();
        int tthdt = thongKe.tthdt();
        int ptdsn = thongKe.ptdsn();
        int ptdst = thongKe.ptdst();
        int ptspn = thongKe.ptspn();
        int ptspt = thongKe.ptspt();
        int pthdn = thongKe.pthdn();
        int pthdt = thongKe.pthdt();
        List<Object[]> dayData = thongKe.dayData();
        List<Object[]> thangData = thongKe.thangData();
        List<Object[]> namData = thongKe.namData();
        Map<String, Object> khoangngayData = thongKe.khoangngayData();
        //xử lý dữ liệu biểu đồ tròn
        List<Map<String, Object>> formattedData = new ArrayList<>();
        for (Object[] row : thongKe.bdTron()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("value", row[1]);
            formattedData.add(map);
        }
        System.out.println(formattedData);
        //xử lý dữ liệu của biểu đồ cột theo ngày
        List<Map<String, Object>> formatteddayData = new ArrayList<>();
        for (Object[] row : thongKe.dayData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formatteddayData.add(map);
        }
        System.out.println(formatteddayData);
        //xử lý dữ liệu của biểu đồ cột theo tháng
        List<Map<String, Object>> formattedthangData = new ArrayList<>();
        for (Object[] row : thongKe.thangData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattedthangData.add(map);
        }
        System.out.println(formattedthangData);
        //xử lý dữ liệu của biểu đồ cột theo năm
        List<Map<String, Object>> formattednamData = new ArrayList<>();
        for (Object[] row : thongKe.namData()) {
            Map<String, Object> map = new HashMap<>();
            map.put("label", row[0]);
            map.put("sp", row[1]);
            map.put("hd", row[2]);
            formattednamData.add(map);
        }
        System.out.println(formattednamData);
        if(startDate != null && endDate != null) {
            System.out.println(startDate);
            System.out.println(endDate);
            List<Object[]> kn = thongKe.khoangngay(startDate, endDate);
            System.out.println(kn);
        }

        model.addAttribute("bdTron", formattedData);
        model.addAttribute("slhdtt", slhdtt);
        model.addAttribute("ttt", ttt);
        model.addAttribute("slhdtn", slhdtn);
        model.addAttribute("ttn", ttn);
        model.addAttribute("sptt", sptt);
        model.addAttribute("sp", sp);
        model.addAttribute("slt", slt);
        model.addAttribute("ttdsn", ttdsn);
        model.addAttribute("ttdst", ttdst);
        model.addAttribute("ttspn", ttspn);
        model.addAttribute("ttspt", ttspt);
        model.addAttribute("tthdn", tthdn);
        model.addAttribute("tthdt", tthdt);
        model.addAttribute("ptdsn", ptdsn);
        model.addAttribute("ptdst", ptdst);
        model.addAttribute("ptspn", ptspn);
        model.addAttribute("ptspt", ptspt);
        model.addAttribute("pthdn", pthdn);
        model.addAttribute("pthdt", pthdt);
        model.addAttribute("dayData", formatteddayData);
        model.addAttribute("thangData", formattedthangData);
        model.addAttribute("namData", formattednamData);
        model.addAttribute("khoangngayData", khoangngayData);
        return "admin/qlthongke";
    }
}
