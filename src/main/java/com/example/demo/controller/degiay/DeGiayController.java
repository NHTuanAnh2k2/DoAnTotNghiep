package com.example.demo.controller.degiay;

import com.example.demo.entity.DeGiay;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.DeGiayRepository;
import com.example.demo.service.impl.DeGiayImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DeGiayController {
    @Autowired
    DeGiayRepository deGiayRepository;
    @Autowired
    DeGiayImp deGiayImp;

    @GetMapping("/listdegiay")
    public String listdegiay(Model model, @ModelAttribute("degiay") DeGiay deGiay, @ModelAttribute("tim") ThuocTinhInfo info) {
        List<DeGiay> list;

        // Trim khoảng trắng ở đầu và cuối
        String trimmedKey = (info.getKey() != null) ? info.getKey().trim() : null;

        boolean isKeyEmpty = (trimmedKey == null || trimmedKey.isEmpty());
        boolean isTrangthaiNull = (info.getTrangthai() == null);

        if (isKeyEmpty && isTrangthaiNull) {
            list = deGiayRepository.getAll();
        } else {
            list = deGiayImp.getDeGiayByTen(trimmedKey);
        }

        List<DeGiay> listAll = deGiayRepository.findAll();
        model.addAttribute("listAll", listAll);
        model.addAttribute("list", list);
        model.addAttribute("fillSearch", trimmedKey);
        model.addAttribute("fillTrangThai", info.getTrangthai());
        return "admin/qldegiay";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id) {
        deGiayRepository.updateTrangThaiToFalseById(id);
        return "redirect:/listdegiay";
    }


//    @GetMapping("/updateDeGiay/{id}")
//    public String delete(@PathVariable Integer id, Model model) {
//        DeGiay degiay = deGiayRepository.findById(id).orElse(null);
//        model.addAttribute("degiay", degiay);
//        return "admin/qldegiay";
//    }

    @PostMapping("/updateDeGiay")
    public String updateDeGiay(@ModelAttribute("degiay") DeGiay degiay) {
        DeGiay existingDeGiay = deGiayRepository.findById(degiay.getId()).orElse(null);
        if (existingDeGiay != null) {
            existingDeGiay.setTen(degiay.getTen());
            existingDeGiay.setLancapnhatcuoi(LocalDateTime.now());
            existingDeGiay.setNguoicapnhat("DuyNV");
            deGiayRepository.save(existingDeGiay);
        }
        return "redirect:/admin/qldegiay";
    }


    @PostMapping("/addSave")
    public String addSave(@ModelAttribute("degiay") DeGiay deGiay) {
        LocalDateTime currentTime = LocalDateTime.now();
        String trimmedName = (deGiay.getTen() != null) ? deGiay.getTen().trim() : null;
        deGiay.setTen(trimmedName);
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/listdegiay";
    }


    @PostMapping("/addDeGiayModal")
    public String addDeGiayModal(@ModelAttribute("degiay") DeGiay deGiay) {
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/viewaddSPGET";
    }

    @PostMapping("/addDeGiaySua")
    public String addDeGiaySua(@ModelAttribute("degiay") DeGiay deGiay, @RequestParam("spctId") Integer spctId) {
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setTrangthai(true);
        deGiay.setNgaytao(currentTime);
        deGiay.setLancapnhatcuoi(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/updateCTSP/" + spctId;
    }

    @ModelAttribute("dsdg")
    public List<DeGiay> getDS() {
        return deGiayImp.findAll();
    }


}
