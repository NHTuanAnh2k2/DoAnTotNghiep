package com.example.demo.controller.mausac;


import com.example.demo.entity.DeGiay;
import com.example.demo.entity.MauSac;
import com.example.demo.info.ThuocTinhInfo;
import com.example.demo.repository.MauSacRepository;
import com.example.demo.service.impl.MauSacImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MauSacController {
    @Autowired
    MauSacImp mauSacImp;
    @Autowired
    MauSacRepository mauSacRepository;

    @GetMapping("/listMauSac")
    public String listMauSac(Model model, @ModelAttribute("mausac") MauSac mauSac, @ModelAttribute("tim") ThuocTinhInfo info) {
        List<MauSac> page = null;
        if (info.getKey() != null) {
            page = mauSacRepository.getDeGiayByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            page = mauSacRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("list", page);
        return "admin/qlmausac";
    }



    @PostMapping("/addMauSacModal")
    public String addMauSacModal(@ModelAttribute("mausac") MauSac mauSac) {
        mauSac.setTrangthai(true);
        mauSacImp.addMauSac(mauSac);
        return "redirect:/viewaddSPGET";
    }

}
