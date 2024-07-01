package com.example.demo.controller.thuonghieu;

import com.example.demo.entity.DeGiay;
import com.example.demo.entity.ThuongHieu;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.ThuongHieuRepository;
import com.example.demo.service.impl.ThuongHieuImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class ThuongHieuController {
    @Autowired
    ThuongHieuRepository thuongHieuRepository;
    @Autowired
    ThuongHieuImp thuongHieuImp;

    @GetMapping("/listthuonghieu")
    public String hienthi(@RequestParam(defaultValue = "0") int p, @ModelAttribute("th") ThuocTinhInfo info, @ModelAttribute("thuonghieu") ThuongHieu thuongHieu, Model model) {
        List<ThuongHieu> list;

        boolean isKeyEmpty = (info.getKey() == null || info.getKey().trim().isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            list = thuongHieuRepository.findAllByOrderByNgaytaoDesc();
        } else {
            list = thuongHieuImp.getThuongHieuByTenOrTrangthai(info.getKey(), info.getTrangthai());
        }

        model.addAttribute("page", list);
        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/qlthuonghieu";
    }


    @PostMapping("/addSaveThuongHieu")
    @CacheEvict(value = "thuonghieuCache", allEntries = true)
    public String addSave(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, @ModelAttribute("th") ThuocTinhInfo info, Model model) {
        if (thuongHieuRepository.existsByTen(thuongHieu.getTen())) {
            model.addAttribute("errThuongHieu", "Tên thương hiệu trùng!");
            return "admin/qlthuonghieu";
        }
        LocalDateTime currentTime = LocalDateTime.now();
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieuImp.add(thuongHieu);
        return "redirect:/listthuonghieu";
    }


    @PostMapping("/addThuongHieuModal")
    public String addThuongHieuModal(@ModelAttribute("thuonghieu") ThuongHieu thuongHieu, @ModelAttribute("th") ThuocTinhInfo info) {
        LocalDateTime currentTime = LocalDateTime.now();
        thuongHieu.setTrangthai(true);
        thuongHieu.setNgaytao(currentTime);
        thuongHieu.setLancapnhatcuoi(currentTime);
        thuongHieuImp.add(thuongHieu);
        return "redirect:/viewaddSPGET";
    }

}
