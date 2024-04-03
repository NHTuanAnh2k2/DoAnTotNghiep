package com.example.demo.controller.phieugiamgia;

import com.example.demo.entity.PhieuGiamGia;
import com.example.demo.service.impl.PhieuGiamGiaImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Controller
public class PhieuGiamGiaController {
    @Autowired
    private PhieuGiamGiaImp phieuGiamGiaImp;

    @GetMapping("/admin/qldotgiamgia")
    public String qldotgiamgia() {
        return "/admin/qldotgiamgia";
    }

    @GetMapping("/admin/adddotgiamgia")
    public String adddotgiamgia() {
        return "/admin/adddotgiamgia";
    }

    @GetMapping("/admin/hien-thi-phieu-giam-gia")
    public String qlphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia, Model model,@RequestParam(defaultValue = "0") Integer p) {
        Pageable pageable = PageRequest.of(p, 15);
        Page<PhieuGiamGia> pagePGG = phieuGiamGiaImp.findAll(pageable);
        model.addAttribute("pagePGG",pagePGG);
        return "admin/qlphieugiamgia";
    }
    @GetMapping("/admin/xem-them-phieu-giam-gia")
    public String qlxemthemphieugiamgia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia){
        return "admin/addphieugiamgia";
    }
    @PostMapping("/admin/them-phieu-giam-gia")
    public String AddPhieuGiamGia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc){

        phieuGiamGia.setNguoitao("Tuan Anh");
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);

        phieuGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
        System.out.println(phieuGiamGia.isLoaiphieu());
        return "redirect:/admin/hien-thi-phieu-giam-gia";
    }
    @GetMapping("/admin/chi-tiet-phieu-giam-gia/{Id}")
    public String ChiTietPhieuGiamGia(@PathVariable("Id") Integer Id, Model model){
        Optional<PhieuGiamGia> phieuGiamGia= phieuGiamGiaImp.findPhieuGiamGiaById(Id);
        model.addAttribute("phieuGiamGiaCT",phieuGiamGia);
        return "redirect:/admin/hien-thi-phieu-giam-gia";
    }

    @PostMapping("/admin/cap-nhat-phieu-giam-gia/{Id}")
    public String CapNhatPhieuGiamGia(@ModelAttribute("phieuGiamGia") PhieuGiamGia phieuGiamGia,
                                  @RequestParam("ngayBatDau") String ngayBatDau,
                                  @RequestParam("ngayKetThuc") String ngayKetThuc){

        phieuGiamGia.setNguoitao("Tuan Anh");
        Timestamp ngayBatDauTimestamp = Timestamp.valueOf(ngayBatDau.replace("T", " ") + ":00");
        Timestamp ngayKetThucTimestamp = Timestamp.valueOf(ngayKetThuc.replace("T", " ") + ":00");
        phieuGiamGia.setNgaybatdau(ngayBatDauTimestamp);
        phieuGiamGia.setNgayketthuc(ngayKetThucTimestamp);

        phieuGiamGia.setNgaytao(new Timestamp(System.currentTimeMillis()));
        phieuGiamGiaImp.AddPhieuGiamGia(phieuGiamGia);
        System.out.println(phieuGiamGia.isLoaiphieu());
        return "redirect:/admin/hien-thi-phieu-giam-gia";
    }
}
