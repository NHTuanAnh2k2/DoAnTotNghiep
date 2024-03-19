package com.example.demo.controller.degiay;


import com.example.demo.entity.DeGiay;
import com.example.demo.info.DeGiayInfo;
import com.example.demo.service.impl.DeGiayImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class DeGiayController {
    @Autowired
    DeGiayImp deGiayImp;

    @GetMapping("/listdegiay")
    public String listdegiay(Model model, @ModelAttribute("degiay") DeGiay deGiay, @ModelAttribute("tim") DeGiayInfo info) {
        List<DeGiay> page = null;
        if (info.getKey() != null) {
            page = deGiayImp.getDeGiayByTen(info.getKey());
        } else {
            page = deGiayImp.getAll();
        }
        model.addAttribute("list", page);
        return "admin/qldegiay";
    }

    @PostMapping("/addSave")
    public String addSave(Model model, @ModelAttribute("degiay") DeGiay deGiay) {
        deGiayImp.add(deGiay);
        return "redirect:/listdegiay";
    }

    @ModelAttribute("dsdg")
    public List<DeGiay> getDS() {
        return deGiayImp.getAll();
    }


}
