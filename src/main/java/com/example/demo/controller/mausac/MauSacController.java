package com.example.demo.controller.mausac;


import com.example.demo.entity.MauSac;
import com.example.demo.service.impl.MauSacImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MauSacController {
    @Autowired
    MauSacImp mauSacImp;

    @PostMapping("/addMauSacModal")
    public String addMauSacModal(@ModelAttribute("mausac") MauSac mauSac) {
        mauSacImp.addMauSac(mauSac);
        return "redirect:/viewaddSP";
    }
}
