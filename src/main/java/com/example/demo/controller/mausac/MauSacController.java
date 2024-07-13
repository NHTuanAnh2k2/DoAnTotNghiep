package com.example.demo.controller.mausac;

import com.example.demo.entity.MauSac;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.service.impl.MauSacImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class MauSacController {
    @Autowired
    MauSacImp mauSacImp;
    @Autowired
    MauSacRepository mauSacRepository;

    @GetMapping("/listMauSac")
    public String listMauSac(Model model, @ModelAttribute("mausac") MauSac mauSac, @ModelAttribute("tim") ThuocTinhInfo info) {
        List<MauSac> page;

        boolean isKeyEmpty = (info.getKey() == null || info.getKey().trim().isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            page = mauSacRepository.getAll();
        } else {
            page = mauSacRepository.findMauSacByTenAndTrangThaiFalse(info.getKey());
        }

        model.addAttribute("fillSearch", info.getKey());
        model.addAttribute("fillTrangThai", info.getTrangthai());
        model.addAttribute("list", page);
        return "admin/qlmausac";
    }
    @PostMapping("/updateMauSac/{id}")
    public String updateMauSac(@PathVariable Integer id) {
        mauSacRepository.updateTrangThaiToFalseById(id);
        return "redirect:/listMauSac";
    }

    @PostMapping("/addSaveMauSac")
    @CacheEvict(value = "mausacCache", allEntries = true)
    public String addSave(@ModelAttribute("mausac") MauSac mauSac, @ModelAttribute("ms") ThuocTinhInfo info, Model model) {
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSacRepository.save(mauSac);
        return "redirect:/listMauSac";
    }



    @PostMapping("/addMauSacModal")
    public String addMauSacModal(@ModelAttribute("mausac") MauSac mauSac) {
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSacImp.addMauSac(mauSac);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addMauSacSua")
    public String addMauSacSua(@ModelAttribute("mausac") MauSac mauSac, @RequestParam("spctId") Integer spctId) {
        LocalDateTime currentTime = LocalDateTime.now();
        mauSac.setTrangthai(true);
        mauSac.setNgaytao(currentTime);
        mauSac.setLancapnhatcuoi(currentTime);
        mauSacImp.addMauSac(mauSac);
        return "redirect:/updateCTSP/" + spctId;
    }

}
