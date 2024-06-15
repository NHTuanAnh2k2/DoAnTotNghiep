package com.example.demo.controller.thongke;

import com.example.demo.entity.HoaDon;
import com.example.demo.entity.HoaDonChiTiet;
import com.example.demo.service.impl.HoaDonChiTietImp;
import com.example.demo.service.impl.HoaDonImp;
import com.example.demo.service.impl.ThongKeImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class ThongKeController {
    @Autowired
    ThongKeImpl thongKe;

    @GetMapping("/admin/qlthongke")
    public String admin(Model model) {
        List<Object[]>  sp = thongKe.soLuongDaBan();
        int slhdtt = thongKe.thongKeTheoThang();
        int ttt = thongKe.thongKeTienTheoThang();
        int slhdtn = thongKe.thongKeTheoNgay();
        int ttn = thongKe.thongKeTienTheoNgay();
        int sptt = thongKe.soLuongsp();
        List<Object[]> bdTron = thongKe.bdTron();
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
        Map<String, Object> dayData = thongKe.dayData();
        Map<String, Object> thangData = thongKe.thangData();
        Map<String, Object> namData = thongKe.namData();
        Map<String, Object> khoangngayData = thongKe.khoangngayData();

        model.addAttribute("slhdtt", slhdtt);
        model.addAttribute("ttt", ttt);
        model.addAttribute("slhdtn", slhdtn);
        model.addAttribute("ttn", ttn);
        model.addAttribute("sptt", sptt);
        model.addAttribute("sp", sp);
        model.addAttribute("slt", slt);
        model.addAttribute("bdTron", bdTron);
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
        model.addAttribute("dayData", dayData);
        model.addAttribute("thangData", thangData);
        model.addAttribute("namData", namData);
        model.addAttribute("khoangngayData", khoangngayData);
        return "admin/qlthongke";
    }
}
