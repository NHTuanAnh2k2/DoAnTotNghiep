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
        List<DeGiay> page = null;
        if (info.getKey() != null) {
            page = deGiayImp.getDeGiayByTenOrTrangthai(info.getKey(), info.getTrangthai());
        } else {
            page = deGiayRepository.findAllByOrderByNgaytaoDesc();
        }
        model.addAttribute("list", page);
        return "admin/qldegiay";
    }

    @GetMapping("/update/{id}")
    public String viewUpdate(@PathVariable Integer id, Model model) {
        model.addAttribute("degiay", deGiayImp.findById(id));
        return "admin/updategiay";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute("degiay") DeGiay deGiay) {
        deGiay.setId(id);
        deGiayImp.add(deGiay);
        return "redirect:/listdegiay";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        deGiayImp.delete(id);
        return "redirect:/listdegiay";
    }

    @PostMapping("/addSave")
    public String addSave(@ModelAttribute("degiay") DeGiay deGiay, RedirectAttributes redirectAttributes) {
        if (deGiay.getTen().equals("")) {
            redirectAttributes.addFlashAttribute("err", "Tên không được để trống");
            return "admin/qldegiay";
        }
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setNgaytao(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/listdegiay";
    }

    @PostMapping("/addDeGiayModal")
    public String addDeGiayModal(@ModelAttribute("degiay") DeGiay deGiay) {
        LocalDateTime currentTime = LocalDateTime.now();
        deGiay.setNgaytao(currentTime);
        deGiayImp.add(deGiay);
        return "redirect:/viewaddSPGET";
    }

    @ModelAttribute("dsdg")
    public List<DeGiay> getDS() {
        return deGiayImp.findAll();
    }


}
